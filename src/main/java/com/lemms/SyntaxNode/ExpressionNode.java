package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.ValueVisitor;

import java.util.List;

public abstract class ExpressionNode extends Node {

    public abstract Object accept(ValueVisitor visitor);

    public static ExpressionNode parse(List<Token> expressionTokens) {
        // toDO: Parsing Expression to other Nodes like BinaryOperator or single Value
        // Node
        // Note the Precedence and Association!

        return null;
    }

}
