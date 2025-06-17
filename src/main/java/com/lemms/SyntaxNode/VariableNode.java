package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.TokenType;
import com.lemms.interpreter.ValueVisitor;

public class VariableNode extends ExpressionNode{
    public String name;
    public VariableNode child;    

    public VariableNode(String identifierName) {
        this.name = identifierName;
    }

    public VariableNode(String identifierName, VariableNode child) {
        this.name = identifierName;
        this.child = child;
    }

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }
}
