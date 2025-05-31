package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.StatementVisitor;

import java.util.ArrayList;

public class ElifNode extends ConditionedBlock {
    public ElifNode(ExpressionNode condition, StatementNode thenBlock) {
        super(condition, thenBlock);
    }

    @Override
    public void accept(StatementVisitor visitor) {

    }
}
