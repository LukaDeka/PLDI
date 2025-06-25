package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.TokenType;
import com.lemms.interpreter.ValueVisitor;
import com.lemms.interpreter.object.LemmsData;

public class VariableNode extends ExpressionNode {
    public String name;

    public VariableNode(String identifierName) {
        this.name = identifierName;
    }
    
    @Override
    public LemmsData accept(ValueVisitor visitor) {
        return visitor.visitVariableValue(this);
    }
}
