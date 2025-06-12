package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.TokenType;
import com.lemms.interpreter.ValueVisitor;

public class VariableNode extends ExpressionNode{
    public String name;
    public Token token;  //Line

    public VariableNode(Token identifierToken) {
        if (identifierToken == null || identifierToken.getType() != TokenType.IDENTIFIER) {
            throw new IllegalArgumentException("VariableNode muss mit einem IDENTIFIER-Token erstellt werden.");
        }

        this.name = identifierToken.getValue();
        this.token = identifierToken; //Line
    }

    public VariableNode(String identifierName) {
        this.name = identifierName;
    }

    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }
}
