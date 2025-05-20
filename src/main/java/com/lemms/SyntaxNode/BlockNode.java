package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.StatementVisitor;

public class BlockNode extends StatementNode {
    List<StatementNode> statements;

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitBlockStatement(this);
    }
}
