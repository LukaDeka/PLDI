package com.lemms.parser;

import com.lemms.Exceptions.MissingTokenException;
import com.lemms.Exceptions.UnexpectedToken;
import com.lemms.SyntaxNode.ExpressionNode;
import com.lemms.SyntaxNode.FunctionCallNode;
import com.lemms.SyntaxNode.LiteralNode;
import com.lemms.SyntaxNode.MemberAccessNode;
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
                return green + record.getMessage() + reset + "\n"; // z.B. nur die Nachricht ausgeben
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
    public Token consume() {
        if (pos < tokens.size()) {
            return tokens.get(pos++);
        }
        // Spezielles EOF Token statt exception?
        throw new RuntimeException("Unexpected end of token stream at position " + pos);
    }

    public Token peek() {
        // Spezielles EOF Token statt null?
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    /*
     * - verschiedene Stufen und Methoden basierend auf PRECEDENCE
     * - ASSOCIATIVITY innerhalb der Stufen von links nach rechts
     * 0. parseExpression (entrypoint - public method)
     * 1. parseLogicalOrExpression (für OR)
     * 2. parseLogicalAndExpression (für AND)
     * 3. parseComparisonExpression (für >, <, >=, <=, EQ, NEQ)
     * 4. parseAdditiveExpression (für binäres +, -)
     * 5. parseMultiplicativeTerm (für *, /, MODULO)
     * 6. parseUnaryFactor (für Literale, (), unäres +, unäres -, NOT)
     */

    // parseExpression - public entry method
    public ExpressionNode parseExpression() {
        logger.info("\n===== STARTING PARSE EXPRESSION =====");
        ExpressionNode node = parseLogicalOrExpression();
        logger.info("\n===== FINISHED PARSE EXPRESSION =====");
        return node;
    }

    private FunctionCallNode parseFunctionCall() {
        // Expect IDENTIFIER (function name)
        Token identifier = consume();
        if (identifier.getType() != TokenType.IDENTIFIER) {
            throw new UnexpectedToken("Expected function name (identifier), found: " + identifier.getType());
        }
        // Expect '('
        Token openParen = consume();
        if (openParen.getType() != TokenType.BRACKET_OPEN) {
            throw new MissingTokenException("Expected '(' after function name.");
        }
        List<ExpressionNode> params = new ArrayList<>();
        if (peek() != null && peek().getType() != TokenType.BRACKET_CLOSED) {
            do {
                params.add(parseExpression());
            } while (peek() != null && peek().getType() == TokenType.COMMA && consume() != null);
        }
        Token closeParen = consume();
        if (closeParen.getType() != TokenType.BRACKET_CLOSED) {
            throw new MissingTokenException("Expected ')' after function arguments.");
        }
        FunctionCallNode functionCallNode = new FunctionCallNode();
        functionCallNode.functionName = identifier.getValue();
        functionCallNode.params = params;
        return functionCallNode;
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

    private ExpressionNode parseMultiplicativeTerm() {
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
            current = peek(); // redundant?
        }

        logger.info("\n----- TERM PARSING -----");
        return leftFactor;
    }

    private ExpressionNode parseBaseFactor() {
        Token current = peek();
        if (current == null) {
            throw new UnexpectedToken("Unexpected end of input");
        }

        switch (current.getType()) {
            case IDENTIFIER -> {
                if (pos + 1 < tokens.size() && tokens.get(pos + 1).getType() == TokenType.BRACKET_OPEN) {
                    return parseFunctionCall();
                } else {
                    Token token = consume();
                    return new VariableNode(token.getValue());
                }
            }
            case BRACKET_OPEN -> {
                consume(); // consume '('
                ExpressionNode expr = parseExpression();
                if (peek() == null || peek().getType() != TokenType.BRACKET_CLOSED) {
                    throw new MissingTokenException("Expected ')'");
                }
                consume(); // consume ')'
                return expr;
            }
            default -> throw new UnexpectedToken("Unexpected token: " + current.getType());
        }
    }

    private ExpressionNode parseMemberAccess() {
        ExpressionNode base = parseBaseFactor();

        // If no dot follows, just return the base factor
        if (peek() == null || peek().getType() != TokenType.DOT) {
            return base;
        }

        // Build the member access chain
        MemberAccessNode root = null;
        MemberAccessNode current = null;

        while (peek() != null && peek().getType() == TokenType.DOT) {
            consume(); // consume '.'

            Token memberToken = consume();
            if (memberToken.getType() != TokenType.IDENTIFIER) {
                throw new UnexpectedToken("Expected identifier after '.'");
            }

            ExpressionNode memberExpression;
            if (peek() != null && peek().getType() == TokenType.BRACKET_OPEN) {
                // Backtrack and parse as function call
                pos--;
                memberExpression = parseFunctionCall();
            } else {
                memberExpression = new VariableNode(memberToken.getValue());
            }

            // Create new MemberAccessNode for this access
            MemberAccessNode newAccess = new MemberAccessNode(memberExpression, null);

            if (root == null) {
                // First member access - base becomes the object
                root = new MemberAccessNode(base, newAccess);
                current = newAccess; // Fix: current should point to the newAccess, not root
            } else {
                // Chain subsequent accesses
                current.child = newAccess;
                current = newAccess; // Move current to the new access
            }
        }

        return root;
    }

    private ExpressionNode parseUnaryFactor() {
        logger.info("\n+++++ FACTOR PARSING +++++");
        Token current = peek();
        // logger.info(current + " [current]");

        if (current == null) {
            logger.info("END");
            return null;
        }

        switch (current.getType()) {
            // UNARY Operator (e.g. +5 or -5 -> 0+5 or 0-5)
            case PLUS -> {
                // ignore PLUS and continue parsing following factor (because +x is same as x)
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

            // usual case: just a literalNode
            case INT, STRING, BOOL -> {
                Token literalToken = consume();
                logger.info(current + "\n----- LITERAL NODE CREATED -----");
                if (current.getType() == TokenType.INT) {
                    return new LiteralNode(Integer.parseInt(literalToken.getValue()));
                } else if (current.getType() == TokenType.BOOL) {
                    return new LiteralNode(Boolean.parseBoolean(literalToken.getValue()));
                } else if (current.getType() == TokenType.STRING) {
                    return new LiteralNode(literalToken.getValue());
                }

                throw new UnexpectedToken("Unexpected Token: " + current.getType());
            }

            case IDENTIFIER -> {
                return parseMemberAccess();
            }

            // if bracketed, then parse new Expression (recursive call)
            case BRACKET_OPEN -> {
                logger.info(current + "\n----- [NESTED EXPRESSION] -----");
                consume();
                ExpressionNode expr = parseLogicalOrExpression();

                if (consume().getType() != TokenType.BRACKET_CLOSED)
                    throw new MissingTokenException("Expected BRACKET_CLOSED, found " + current.getType());

                // consum
                return expr;
            }

            // if not UNARY operator, nor normal Literal, or bracketed expression --> then
            // exception
            default -> throw new UnexpectedToken("Unexpected Token: " + current.getType());
        }
    }

    public static void main(String[] args) {
        // Example usage
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(TokenType.IDENTIFIER, "human"));
        tokens.add(new Token(TokenType.DOT));
        tokens.add(new Token(TokenType.IDENTIFIER, "birthDate"));
        tokens.add(new Token(TokenType.DOT));
        tokens.add(new Token(TokenType.IDENTIFIER, "year"));

        ExpressionParser parser = new ExpressionParser(tokens);
        ExpressionNode expression = parser.parseExpression();

        // Print or process the parsed expression
        System.out.println(expression);
    }
}
