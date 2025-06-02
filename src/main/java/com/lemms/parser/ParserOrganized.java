package com.lemms.parser;

import com.lemms.Parser;
import com.lemms.Token;
import com.lemms.TokenType;
import com.lemms.Exceptions.MissingTokenException;
import com.lemms.Exceptions.SyntaxException;
import com.lemms.Exceptions.UnexpectedToken;
import com.lemms.SyntaxNode.*;

import org.slf4j.LoggerFactory;

import static java.lang.Character.reverseBytes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.*;

import static com.lemms.TokenType.*;

public class ParserOrganized {

    private final List<Token> tokens;
    private int position = 0;

    public ParserOrganized(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<StatementNode> parse() {
        List<StatementNode> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseStatement());
        }
        return statements;
    }

    private StatementNode parseStatement() {
        if (match(TokenType.IF))
            return parseIfStatement();
        if (match(TokenType.WHILE))
            return parseWhileStatement();
        if (match(TokenType.IDENTIFIER)) {
            if (peek().getType() == TokenType.ASSIGNMENT) {
                return parseAssignment();
            } else if (peek().getType() == TokenType.BRACKET_OPEN) {
                return parseFunctionCall();
            }
        }
        // ... other statement types ...
        throw error("Unexpected token: " + peek());
    }

    private IfNode parseIfStatement() {
        // 'if' already matched
        consume(TokenType.BRACKET_OPEN, "Expected '(' after 'if'.");
        ExpressionNode condition = parseExpression();
        consume(TokenType.BRACKET_CLOSED, "Expected ')' after if condition.");
        consume(TokenType.BRACES_OPEN, "Expected '{' to start if block.");
        BlockNode ifBlock = parseBlock();
        consume(TokenType.BRACES_CLOSED, "Expected '}' after if block.");

        BlockNode elseBlock = null;
        if (match(TokenType.ELSE)) {
            consume(TokenType.BRACES_OPEN, "Expected '{' to start else block.");
            elseBlock = parseBlock();
            consume(TokenType.BRACES_CLOSED, "Expected '}' after else block.");
        }

        IfNode ifNode = new IfNode();
        ifNode.condition = condition;
        ifNode.ifBody = ifBlock;
        ifNode.elseStatement = elseBlock;

        return ifNode;
    }

    private AssignmentNode parseAssignment() {
        Token identifier = previous();
        consume(TokenType.ASSIGNMENT, "Expected '=' after identifier.");
        ExpressionNode expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after assignment.");
        return new AssignmentNode(new VariableNode(identifier), expr);
    }

    private ExpressionNode parseExpression() {
        // Collect tokens for the expression (until a delimiter, e.g., ')', ';', etc.)
        List<Token> exprTokens = new ArrayList<>();
        while (!isAtEnd() && !isExpressionTerminator(peek())) {
            exprTokens.add(advance());
        }
        return new ExpressionParser(exprTokens).parseExpression();
    }

    private BlockNode parseBlock() {
        List<StatementNode> statements = new ArrayList<>();
        while (!isAtEnd() && !check(TokenType.BRACES_CLOSED)) {
            statements.add(parseStatement());
        }
        return new BlockNode(statements);
    }

    private WhileNode parseWhileStatement() {
        // 'while' already matched
        consume(TokenType.BRACKET_OPEN, "Expected '(' after 'while'.");
        ExpressionNode condition = parseExpression();
        consume(TokenType.BRACKET_CLOSED, "Expected ')' after while condition.");
        consume(TokenType.BRACES_OPEN, "Expected '{' to start while block.");
        BlockNode body = parseBlock();
        consume(TokenType.BRACES_CLOSED, "Expected '}' after while block.");

        WhileNode whileNode = new WhileNode();
        whileNode.condition = condition;
        whileNode.whileBody = body;
        return whileNode;
    }

    private FunctionCallStatementNode parseFunctionCall() {
        Token identifier = previous(); // The function name (IDENTIFIER) was just matched
        consume(TokenType.BRACKET_OPEN, "Expected '(' after function name.");
        List<ExpressionNode> params = new ArrayList<>();
        if (!check(TokenType.BRACKET_CLOSED)) {
            do {
                params.add(parseExpression());
            } while (match(TokenType.COMMA));
        }
        consume(TokenType.BRACKET_CLOSED, "Expected ')' after function arguments.");
        consume(TokenType.SEMICOLON, "Expected ';' after function call.");
        FunctionCallNode functionCallNode = new FunctionCallNode();
        
        functionCallNode.functionName =identifier.getValue();
        functionCallNode.params = params;
        FunctionCallStatementNode functionCallStatementNode = new FunctionCallStatementNode();
        functionCallStatementNode.functionCall = functionCallNode;

        return functionCallStatementNode;
    }

    // Utility methods:
    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return tokens.get(position).getType() == type;
    }

    private Token advance() {
        if (!isAtEnd())
            position++;
        return previous();
    }

    private Token peek() {
        return tokens.get(position);
    }

    private Token previous() {
        return tokens.get(position - 1);
    }

    private boolean isAtEnd() {
        return position >= tokens.size();
    }

    private RuntimeException error(String message) {
        return new RuntimeException(message);
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        throw error(message);
    }

    private boolean isExpressionTerminator(Token token) {
        return token.getType() == TokenType.BRACKET_CLOSED
                || token.getType() == TokenType.SEMICOLON
                || token.getType() == TokenType.BRACES_CLOSED;
    }
}