package com.lemms.SyntaxNode;

import com.lemms.Token;

import java.util.ArrayList;

public class ElifNode extends ConditionedBlock {
    public ElifNode(ExpressionNode condition, StatementNode thenBlock) {
        super(condition, thenBlock);
    }
}
