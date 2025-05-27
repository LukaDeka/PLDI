package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

public class VariableNode extends ExpressionNode {
    public String name; //saving token value OR entire token?

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }

    public VariableNode(String identifierName) {
        this.name = identifierName;
    }
}
