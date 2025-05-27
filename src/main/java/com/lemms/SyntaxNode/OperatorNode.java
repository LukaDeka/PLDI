package com.lemms.SyntaxNode;

import com.lemms.Token;
import java.util.List;
import com.lemms.Token.TokenType;
import com.lemms.interpreter.ValueVisitor;

public class OperatorNode extends ExpressionNode {
    
    
    public Object operator;
    public ExpressionNode leftOperand;
    public ExpressionNode rightOperand;

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitOperatorValue(this);
    }

    public OperatorNode() {}

    public OperatorNode(ExpressionNode left, Token operator, ExpressionNode right) {
        super();
    }
}
