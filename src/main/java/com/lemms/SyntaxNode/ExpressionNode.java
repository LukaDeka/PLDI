package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;
import com.lemms.interpreter.object.LemmsData;

public abstract class ExpressionNode extends Node {

    public abstract LemmsData accept(ValueVisitor visitor);

}
