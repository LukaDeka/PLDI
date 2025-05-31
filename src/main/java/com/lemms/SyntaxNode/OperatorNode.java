package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

public class OperatorNode extends ExpressionNode {

    public ExpressionNode leftOperand;
    public Token operator;
    public ExpressionNode rightOperand;

    public OperatorNode(ExpressionNode left, Token operator, ExpressionNode right) {
        super();
        this.leftOperand = left;
        this.operator = operator;
        this.rightOperand = right;
    }

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitOperatorValue(this);
    }
}
