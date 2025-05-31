package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

public class VariableNode extends ExpressionNode{
    public String name;

    public VariableNode(Token identifierToken) {
        this.name = identifierToken.getValue();
    }

    public VariableNode(String identifierName) {
        this.name = identifierName;
    }

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }
}
