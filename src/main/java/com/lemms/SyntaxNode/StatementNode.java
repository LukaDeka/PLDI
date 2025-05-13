package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;

public abstract class StatementNode extends Node {
    public abstract void accept(StatementVisitor visitor);
}
