package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

public class LiteralNode extends ExpressionNode {
    public final Object value;

    public LiteralNode(Token value) {
        this.value = value.getValue();
    }

    public LiteralNode(String value) {
        this.value = value;
    }

    public LiteralNode(int value) {
        this.value = value;
    }

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitLiteralValue(this);
    }
}
