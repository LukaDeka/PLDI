package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;

public class WhileNode extends StatementNode {
    public ValueNode condition;
    public StatementNode statement;
    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitWhileStatement(this);
    }
}
