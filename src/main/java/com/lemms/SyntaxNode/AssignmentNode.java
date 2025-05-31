package com.lemms.SyntaxNode;

import com.lemms.Exceptions.SyntaxException;
import com.lemms.Token;
import com.lemms.TokenType;
import com.lemms.parser.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

public class AssignmentNode extends StatementNode {
    public VariableNode left;
    public ExpressionNode right;

    public AssignmentNode(VariableNode identifier, ExpressionNode expression) {
        this.left = identifier;
        this.right = expression;
    }

    public AssignmentNode(ArrayList<Token> tokens) {
        //checkTokenList(tokens,size-> size >= 3, TokenType.IDENTIFIER, TokenType.ASSIGNMENT);

        logger.info(tokens + "\n----- ASSIGNMENT -----");
        Token identifier = tokens.get(0);
        ArrayList<Token> expressionTokens = new ArrayList<>(tokens.subList(2, tokens.size()));

        logger.info(identifier + "\n----- VARIABLE -----");
        VariableNode variableNode = new VariableNode(identifier);
        logger.info(expressionTokens + "\n----- EXPRESSION -----");
        ExpressionNode expressionNode = new ExpressionParser(expressionTokens).parseExpression();

        this.left = variableNode;
        this.right = expressionNode;
    }

}
