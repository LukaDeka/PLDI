package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;

public class LiteralNode extends ValueNode {

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitLiteralValue(this);
    }
    
}
