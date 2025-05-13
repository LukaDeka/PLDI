package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;
import com.lemms.interpreter.ValueVisitor;

public abstract class ValueNode extends Node {
    public abstract Object accept(ValueVisitor visitor);
}
