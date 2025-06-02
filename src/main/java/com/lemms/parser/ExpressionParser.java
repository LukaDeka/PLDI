package com.lemms.parser;

import com.lemms.Exceptions.MissingTokenException;
import com.lemms.Exceptions.UnexpectedToken;
import com.lemms.SyntaxNode.ExpressionNode;
import com.lemms.SyntaxNode.LiteralNode;
import com.lemms.SyntaxNode.OperatorNode;
import com.lemms.SyntaxNode.VariableNode;
import com.lemms.Token;
import com.lemms.TokenType;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.lemms.TokenType.*;

public class ExpressionParser {
    protected static final java.util.logging.Logger logger = Logger.getLogger(ExpressionParser.class.getName());
    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                String green = "\u001B[32m";
                String reset = "\u001B[0m";
                return green + record.getMessage() + reset+ "\n"; // z.B. nur die Nachricht ausgeben
            }
        });
        logger.setUseParentHandlers(false); // verhindert doppelte Logs
        logger.addHandler(handler);
        logger.setLevel(Level.OFF);
    }

    private int pos = 0;
    private final List<Token> tokens;

    public ExpressionParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Helfer Function
    public Token consume(){
        if (pos < tokens.size()) {
            return tokens.get(pos++);
        }
        //Spezielles EOF Token statt exception?
        throw new RuntimeException("Unexpected end of token stream at position " + pos);
    }

    public Token peek(){
        //Spezielles EOF Token statt null?
        return pos < tokens.size() ? tokens.get(pos) : null;
    }


    /*
    - verschiedene Stufen und Methoden basierend auf PRECEDENCE
    - ASSOCIATIVITY innerhalb der Stufen von links nach rechts
        0. parseExpression (entrypoint - public method)
        1. parseLogicalOrExpression (für OR)
        2. parseLogicalAndExpression (für AND)
        3. parseComparisonExpression (für >, <, >=, <=, EQ, NEQ)
        4. parseAdditiveExpression (für binäres +, -)
        5. parseMultiplicativeTerm (für *, /, MODULO)
        6. parseUnaryFactor (für Literale, (), unäres +, unäres -, NOT)*/


    // parseExpression - public entry method
    public ExpressionNode parseExpression() {
        logger.info("\n===== STARTING PARSE EXPRESSION =====");
        ExpressionNode node = parseLogicalOrExpression();
        logger.info("\n===== FINISHED PARSE EXPRESSION =====");
        return node;
    }

    private ExpressionNode parseLogicalOrExpression() {
        logger.info("\n+++++ LOGICAL OR EXPRESSION PARSING +++++");
        ExpressionNode left = parseLogicalAndExpression();

        Token current = peek();
        while (current != null && current.getType() == TokenType.OR) {
            Token operator = consume();
            logger.info(operator + " [operator]");
            ExpressionNode right = parseLogicalAndExpression();

            logger.info("\n----- (LOGICAL_OR) OPERATOR NODE CREATED -----");
            left = new OperatorNode(left, operator, right);
            current = peek();
        }
        logger.info("\n----- LOGICAL OR EXPRESSION PARSING -----");
        return left;
    }


    private ExpressionNode parseLogicalAndExpression() {
        logger.info("\n+++++ LOGICAL AND EXPRESSION PARSING +++++");
        ExpressionNode left = parseComparisonExpression();

        Token current = peek();
        while (current != null && current.getType() == TokenType.AND) {
            Token operator = consume();
            logger.info(operator + " [operator]");
            ExpressionNode right = parseComparisonExpression();

            logger.info("\n----- (LOGICAL_AND) OPERATOR NODE CREATED -----");
            left = new OperatorNode(left, operator, right);
            current = peek();
        }
        logger.info("\n----- LOGICAL AND EXPRESSION PARSING -----");
        return left;
    }

    private ExpressionNode parseComparisonExpression() {
        EnumSet<TokenType> comparisonTokenTypes = EnumSet.of(EQ, NEQ, GEQ, LEQ, GT, LT);
        logger.info("\n+++++ COMPARISON EXPRESSION PARSING +++++");
        ExpressionNode left = parseAdditiveExpression(); // Nächsthöhere Präzedenz

        Token current = peek();
        while (current != null && comparisonTokenTypes.contains(current.getType())) {
            Token operator = consume();
            logger.info(operator + " [operator]");
            ExpressionNode right = parseAdditiveExpression();

            logger.info("\n----- (COMPARISON) OPERATOR NODE CREATED -----");
            left = new OperatorNode(left, operator, right);
            current = peek();
        }
        logger.info("\n----- COMPARISON EXPRESSION PARSING -----");
        return left;
    }

    private ExpressionNode parseAdditiveExpression() {
        EnumSet<TokenType> additiveTokenTypes = EnumSet.of(PLUS, MINUS);
        logger.info("\n+++++ ADDITIVE EXPRESSION PARSING +++++");
        ExpressionNode lefTerm = parseMultiplicativeTerm();

        Token current = peek();
        while (current != null && additiveTokenTypes.contains(current.getType())) {
            Token operator = consume();
            logger.info(operator + " [operator]");
            ExpressionNode rightTerm = parseMultiplicativeTerm();

            logger.info("\n----- (ADDITIVE) OPERATOR NODE CREATED -----");
            lefTerm = new OperatorNode(lefTerm, operator, rightTerm);
            current = peek();
        }

        logger.info("\n----- ADDITIVE EXPRESSION PARSING -----");
        return lefTerm;
    }

    private ExpressionNode parseMultiplicativeTerm(){
        EnumSet<TokenType> multiplicationTokenTypes = EnumSet.of(MULTIPLICATION, DIVISION, MODULO);
        logger.info("\n+++++ TERM PARSING +++++");
        ExpressionNode leftFactor = parseUnaryFactor();

        Token current = peek(); 
        while (current != null && multiplicationTokenTypes.contains(current.getType())) {
            Token operator = consume();

            logger.info(operator + " [operator]");
            ExpressionNode rightFactor = parseUnaryFactor();

            logger.info("\n----- (MULTIPLICATIVE) OPERATOR NODE CREATED -----");
            leftFactor = new OperatorNode(leftFactor, operator, rightFactor);
            current = peek(); //redundant?
        }

        logger.info("\n----- TERM PARSING -----");
        return leftFactor;
    }

    private ExpressionNode parseUnaryFactor() {
        logger.info("\n+++++ FACTOR PARSING +++++");
        Token current = peek();
//        logger.info(current + " [current]");

        if (current == null) {
            logger.info("END");
            return null;
        }

        switch (current.getType()) {
            //UNARY Operator (e.g. +5 or -5 -> 0+5 or 0-5)
            case PLUS -> {
                //ignore PLUS and continue parsing following factor (because +x is same as  x)
                Token operatorToken = consume();
                logger.info("\n+++++ UNARY OPERATOR CREATED +++++\n" + operatorToken);
                return parseUnaryFactor();
            }
            case NOT -> {
                Token operatorToken = consume();
                logger.info("\n+++++ UNARY OPERATOR CREATED +++++\n" + operatorToken);
                return new OperatorNode(null, operatorToken, parseUnaryFactor());
            }
            case MINUS -> {
                Token operatorToken = consume();
                logger.info("\n+++++ UNARY OPERATOR CREATED +++++\n" + operatorToken);
                return new OperatorNode(new LiteralNode(0), operatorToken, parseUnaryFactor());
            }

            //usual case: just a literalNode
            case INT, STRING, BOOL -> {
                Token literalToken = consume();
                logger.info(current + "\n----- LITERAL NODE CREATED -----");
                return new LiteralNode(literalToken);
            }

            case IDENTIFIER -> {
                Token identifierToken = consume();
                logger.info(identifierToken + "\n----- IDENTIFIER NODE CREATED -----");
                return new VariableNode(identifierToken); // Erzeuge einen IdentifierNode
            }

            //if bracketed, then parse new Expression (recursive call)
            case BRACKET_OPEN -> {
                logger.info(current + "\n----- [NESTED EXPRESSION] -----");
                consume();
                ExpressionNode expr = parseLogicalOrExpression();

                if (consume().getType() != TokenType.BRACKET_CLOSED)
                    throw new MissingTokenException("Expected BRACKET_CLOSED, found " + current.getType());


                //consum
                return expr;
            }

            //if not UNARY operator, nor normal Literal, or bracketed expression --> then exception
            default -> throw new UnexpectedToken("Unexpected Token: " + current.getType());
        }
    }

    public static void main(String[] args) {
        ArrayList<Token> tokens = new ArrayList<>();
//        tokens.add(new Token(null, 1, TokenType.MINUS));
//        tokens.add(new Token("1", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.PLUS));
//        tokens.add(new Token("2", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.PLUS));
//        tokens.add(new Token("3", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.PLUS));
//        tokens.add(new Token("4", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.MULTIPLICATION));
//        tokens.add(new Token("5", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.MINUS));
//        tokens.add(new Token("6", 1, TokenType.INT));

//        tokens.add(new Token(null, 1, TokenType.BRACKET_OPEN));
//        tokens.add(new Token("funny_var2", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.NEQ));
//        tokens.add(new Token("100100134", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.BRACKET_CLOSED));


        //[BRACKET_OPEN, IDENTIFIER(funny_var2), NEQ, INT(100100134), BRACKET_CLOSED]



        ExpressionNode node = new ExpressionParser(tokens).parseExpression();
        System.out.println(node);


    }
}
