package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;

public abstract class ExpressionNode extends Node {

    public abstract Object accept(ValueVisitor visitor);

}
