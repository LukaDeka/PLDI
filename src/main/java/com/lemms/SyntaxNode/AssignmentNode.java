package com.lemms.SyntaxNode;

import com.lemms.Exceptions.SyntaxException;
import com.lemms.Token;
import com.lemms.TokenType;

import java.util.ArrayList;
import java.util.List;

public class AssignmentNode extends StatementNode {
    VariableNode left;
    ExpressionNode right;

    private AssignmentNode(VariableNode identifier, ExpressionNode expression) {
        this.left = identifier;
        this.right = expression;
    }

    public AssignmentNode(ArrayList<Token> tokens) {
        checkTokenList(tokens,size-> size <= 3, TokenType.IDENTIFIER, TokenType.EQ);

        Token identifier = tokens.get(0);
        List<Token> expressionTokens = tokens.subList(2, tokens.size());

        VariableNode variableNode = new VariableNode(identifier);
        ExpressionNode expressionNode = ExpressionNode.parse(expressionTokens); //toDo: important! parsing a list of Tokens --> OperatorNode or ValueNode

        AssignmentNode assignmentNode = new AssignmentNode(variableNode, expressionNode);
    }

//    public AssignmentNode(ArrayList<Token> tokens) {
//        // Check for minimum count of Tokens
//        if (tokens.size() < 3) {
//            throw new SyntaxException("AssignmentNode must have at least 3 tokens, including IDENTIFIER and EQUALSIGN: " + tokens.size());
//        }
//
//        // first Token must be an Identifier
//        Token identifierToken = tokens.get(0);
//        if (identifierToken.getType() != TokenType.IDENTIFIER) {
//            throw new SyntaxException("InvalidArgument: IDENTIFIER Token expected");
//        }
//
//
//        // Zweiten Token als Zuweisungs-Operator überprüfen:
//        Token equalSignToken = tokens.get(1);
//        if (equalSignToken.getType() != TokenType.EQ) {
//            throw new SyntaxException("InvalidArgument: '=' EQUALSIGN Token expected");
//        }
//        List<Token> expressionTokens = tokens.subList(2, tokens.size());
//
//        VariableNode variableNode = new VariableNode(identifierToken);
//        ExpressionNode expressionNode = ExpressionNode.parse(expressionTokens); //Parsing
//
//        AssignmentNode assignmentNode = new AssignmentNode(variableNode, expressionNode);
//    }

//        //Check for correct Tokens
//        if (tokens.size() < 3
//                || tokens.get(0).getType() != TokenType.IDENTIFIER
//                || tokens.get(1).getType() != TokenType.EQ)
//            throw new IllegalArgumentException("AssignmentNode must have 3 tokens, including IDENTIFIER and EQUALSIGN");
//        ValueNode valueNode = new ValueNode(expressionTokens);
//
//        Token identifierToken = tokens.get(0);
//        //Token equalSignToken = tokens.get(1);   // implicit
//        List<Token> expressionTokens = tokens.subList(2, tokens.size());
//
//        VariableNode variableNode = new VariableNode(identifierToken);
//        ValueNode valueNode = new ValueNode(expressionTokens);
//
//        AssignmentNode assignmentNode = new AssignmentNode(variableNode, valueNode);
}
