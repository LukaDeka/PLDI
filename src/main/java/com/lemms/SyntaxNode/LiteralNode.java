package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;
import com.lemms.interpreter.object.LemmsData;

public class LiteralNode extends ExpressionNode {
    public final Object value;    

    public LiteralNode(String value) {
        this.value = value;
    }

    public LiteralNode(int value) {
        this.value = value;
    }

    public LiteralNode(boolean value) {
        this.value = value;
    }

    @Override
    public LemmsData accept(ValueVisitor visitor) {
        return visitor.visitLiteralValue(this);
    }
}
