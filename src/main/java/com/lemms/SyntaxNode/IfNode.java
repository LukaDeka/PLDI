package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;

public class IfNode extends StatementNode {
    ValueNode condition;
    StatementNode statement;
    @Override
    public void accept(StatementVisitor visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
