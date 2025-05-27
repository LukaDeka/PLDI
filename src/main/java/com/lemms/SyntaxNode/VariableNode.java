package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

class VariableNode extends ExpressionNode{
    Token name; //saving token value OR entire token?

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }

    public VariableNode(Token identifierToken) {
        this.identifier = identifierToken;
    }
}
