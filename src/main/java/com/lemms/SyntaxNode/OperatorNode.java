package com.lemms.SyntaxNode;

import com.lemms.Token;

import java.util.List;

public class OperatorNode extends ExpressionNode {
    
    // TODO: What is the type of the operator?
    public Object operator;
    public ExpressionNode leftOperand;
    public ExpressionNode rightOperand;

    public OperatorNode() {}

    public OperatorNode(ExpressionNode left, Token operator, ExpressionNode right) {
        super();
    }
}
