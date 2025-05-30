package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;


public class WhileNode extends ConditionedBlock {
    public WhileNode(ExpressionNode condition, StatementNode thenBlock) {
        super(condition, thenBlock);
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitWhileStatement(this);
    }
}
