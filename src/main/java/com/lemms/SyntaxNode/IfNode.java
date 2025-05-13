package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;

public class IfNode extends StatementNode {
    public ValueNode condition;
    public StatementNode statement;
    public StatementNode elseStatement;
    @Override
    public void accept(StatementVisitor visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
