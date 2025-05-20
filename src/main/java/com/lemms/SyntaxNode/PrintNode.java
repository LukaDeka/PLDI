package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;

public class PrintNode extends StatementNode {
    public ValueNode printValue;

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitPrintStatement(this);
    }
}
