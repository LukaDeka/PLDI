package com.lemms.SyntaxNode;

import com.lemms.Token;

import java.util.ArrayList;

public class WhileNode extends ConditionedBlock {
    public WhileNode(ExpressionNode condition, StatementNode thenBlock) {
        super(condition, thenBlock);
    }
}
