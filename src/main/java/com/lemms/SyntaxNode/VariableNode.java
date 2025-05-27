package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

public class VariableNode extends ExpressionNode {
    public Token name; //saving token value OR entire token?

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }

    public VariableNode(Token identifierName) {
        this.name = identifierName;
    }
}
