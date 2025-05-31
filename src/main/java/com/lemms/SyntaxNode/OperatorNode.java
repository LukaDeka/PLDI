package com.lemms.SyntaxNode;

import com.lemms.Token;

import java.util.List;

public class OperatorNode extends ExpressionNode {
    
    // TODO: What is the type of the operator?
    public ExpressionNode leftOperand;
    public Object operator;
    public ExpressionNode rightOperand;

    public OperatorNode() {}

    public OperatorNode(ExpressionNode left, Token operator, ExpressionNode right) {
        super();
        this.leftOperand = left;
        this.operator = operator;
        this.rightOperand = right;

    }
}
