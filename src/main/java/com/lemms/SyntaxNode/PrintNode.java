package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;

public class PrintNode extends StatementNode {
    public ValueNode printValue;

    @Override
    public void accept(StatementVisitor visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
