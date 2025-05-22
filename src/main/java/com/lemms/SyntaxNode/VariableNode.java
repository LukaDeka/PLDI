package com.lemms.SyntaxNode;

import com.lemms.Token;

class VariableNode extends ExpressionNode{
    Token identifier; //saving token value OR entire token?

    public VariableNode(Token identifierToken) {
        this.identifier = identifierToken;
    }
}
