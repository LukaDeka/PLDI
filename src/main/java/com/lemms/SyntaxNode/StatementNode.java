package com.lemms.SyntaxNode;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;

public abstract class StatementNode extends Node {
    public abstract FlowSignal accept(StatementVisitor visitor);
}
