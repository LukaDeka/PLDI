package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;

public class LiteralNode extends ValueNode {

    public final Object value;
    
    public LiteralNode(Object value) {
        this.value = value;
    }
    
    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitLiteralValue(this);
    }
    
}
