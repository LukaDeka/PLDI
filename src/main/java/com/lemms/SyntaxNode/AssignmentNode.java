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
}
