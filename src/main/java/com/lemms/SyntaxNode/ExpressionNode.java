package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

import java.util.List;

public abstract class ExpressionNode extends Node {

    public abstract Object accept(ValueVisitor visitor);


}
