package com.lemms.SyntaxNode;

import com.lemms.Token;

public class LiteralNode extends ExpressionNode {

    public final Token value;

    public LiteralNode(Token value) {
        this.value = value;
    }

}