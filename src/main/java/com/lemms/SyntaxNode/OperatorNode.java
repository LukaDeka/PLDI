package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;

public class OperatorNode extends ValueNode {    
    
    // TODO: What is the type of the operator?
    public Object operator;
    public ValueNode leftOperand;
    public ValueNode rightOperand;
    
    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitOperatorValue(this);
    }
}
