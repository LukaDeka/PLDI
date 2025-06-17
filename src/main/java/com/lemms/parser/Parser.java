package com.lemms.parser;

import com.lemms.Exceptions.*;
import com.lemms.Token;
import com.lemms.TokenType;
import com.lemms.SyntaxNode.*;

import org.slf4j.LoggerFactory;

import static java.lang.Character.reverseBytes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.*;

import static com.lemms.TokenType.*;

public class Parser {

    private final List<Token> tokens;
    private int position = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<StatementNode> parse() {
        List<StatementNode> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseStatement());
        }
        return statements;
    }

    private StatementNode parseStatement() throws LemmsParseError {
        if (match(TokenType.RETURN))
            return parseReturnStatement();
        if (match(TokenType.IF))
            return parseIfStatement();
        if (match(TokenType.WHILE))
            return parseWhileStatement();
        if (match(TokenType.IDENTIFIER)) {
            TokenType type = peek().getType();
            if (type == TokenType.ASSIGNMENT) {
                return parseAssignment();
            } else if (type == TokenType.BRACKET_OPEN) {
                return parseFunctionCallStatement();
            }
        }
        if (match(FUNCTION)) {
            return parseFunctionDeclaration();
        }
        if (match(CLASS)) {
            return parseClassDeclaration();
        }
        // ... other statement types ...
        throw new LemmsParseError(peek(),"Unexpected token: " + peek());
    }

    private ReturnNode parseReturnStatement() {
        // 'return' already matched
        ExpressionNode expr = null;
        if (!check(TokenType.SEMICOLON)) {
            expr = parseExpression();
        }
        consume(TokenType.SEMICOLON, "Expected ';' after return statement.");
        ReturnNode returnNode = new ReturnNode();
        returnNode.value = expr;
        return returnNode;
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
        ExpressionNode expr = parseExpression();
        consume(TokenType.SEMICOLON, "Expected ';' after assignment.");
        return new AssignmentNode(new VariableNode(identifier.getValue()), expr);
    }

    private ExpressionNode parseExpression() {
        // Collect tokens for the expression (until a delimiter, e.g., ')', ';', etc.)
        List<Token> exprTokens = new ArrayList<>();
        int parenDepth = 0;
        //davor so: while (!isAtEnd() && !isExpressionTerminator(peek())) {
        while (!isAtEnd()) {
            Token token = peek();
            if (token.getType() == TokenType.BRACKET_OPEN) {
                parenDepth++;
            } else if (token.getType() == TokenType.BRACKET_CLOSED) {
                if (parenDepth == 0)
                    break; // This is the closing paren for the statement, not the expression
                parenDepth--;
            } else if (parenDepth == 0 && isExpressionTerminator(token)) {
                break;
            }
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

    private ClassDeclarationNode parseClassDeclaration(){

        ClassDeclarationNode c = new ClassDeclarationNode();
        List<String> vars = new ArrayList<>();
        List<FunctionDeclarationNode> funcs = new ArrayList<>();

        // read class name
        c.className = consume(IDENTIFIER, "Expected className IDENTIFIER after class keyword").getValue();

        // read class localVariables (localVariables have to be first and after them, only function declarations can exist)
        consume(BRACES_OPEN, "Expected '{' after className IDENTIFIER");
        while (!check(BRACES_CLOSED) && !check(FUNCTION)){
            vars.add(consume(IDENTIFIER, "Expected IDENTIFIER after ';'").getValue());
            consume(SEMICOLON, "Expected ';' after IDENTIFIER");
        }

        // read class localFunctions
        while (!check(BRACES_CLOSED)){
            consume(FUNCTION, "Expected function keyword or '}' after localVariables or previous function");
            funcs.add(parseFunctionDeclaration());
        }

        consume(BRACES_CLOSED, "Expected '}' class Body");

        c.localVariables = vars;
        c.localFunctions = funcs;
        return c;
    }

    private FunctionDeclarationNode parseFunctionDeclaration() {

        FunctionDeclarationNode func = new FunctionDeclarationNode();
        ArrayList<String> params = new ArrayList<>();

        // read function name
        func.functionName = consume(IDENTIFIER, "Expected identifier (function name) after 'function' keyword")
                .getValue();

        // read function params
        consume(BRACKET_OPEN, "Expected '(' after function name.");
        if (!check(BRACKET_CLOSED)) {
            do {
                params.add(consume(IDENTIFIER, "Expected IDENTIFIER parameters in function").getValue());
            } while (match(COMMA));
        }
        consume(BRACKET_CLOSED, "Expected ')' after function arguments");

        consume(BRACES_OPEN, "Expected '{' after function header");
        BlockNode body = parseBlock();
        consume(BRACES_CLOSED, "Expected '}' after function body");

        func.paramNames = params;
        func.functionBody = body;
        return func;
    }

    private FunctionCallStatementNode parseFunctionCallStatement() {
        // The IDENTIFIER was just matched
        List<Token> exprTokens = new ArrayList<>();
        exprTokens.add(previous()); // Add the IDENTIFIER

        // Collect tokens until we reach ')'
        int parenDepth = 0;
        do {
            Token token = advance();
            exprTokens.add(token);
            if (token.getType() == TokenType.BRACKET_OPEN)
                parenDepth++;
            if (token.getType() == TokenType.BRACKET_CLOSED)
                parenDepth--;
        } while (parenDepth > 0 && !isAtEnd());

        // Parse the function call as an expression
        ExpressionNode expr = new ExpressionParser(exprTokens).parseExpression();

        consume(TokenType.SEMICOLON, "Expected ';' after function call.");

        // Wrap in statement node
        FunctionCallStatementNode stmt = new FunctionCallStatementNode();
        stmt.functionCall = (FunctionCallNode) expr;
        return stmt;
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
        throw new LemmsParseError(peek(), message);
    }

    private boolean isExpressionTerminator(Token token) {
        return token.getType() == TokenType.BRACKET_CLOSED
                || token.getType() == TokenType.SEMICOLON
                || token.getType() == TokenType.BRACES_CLOSED;
    }
}