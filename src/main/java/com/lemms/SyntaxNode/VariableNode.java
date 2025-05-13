package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;

public class VariableNode extends ValueNode {

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }
    
}
