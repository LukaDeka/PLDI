package com.lemms.SyntaxNode;


abstract public class ConditionedBlock extends StatementNode{
    public ExpressionNode condition;
    public StatementNode statement;

    public ConditionedBlock(ExpressionNode condition, StatementNode statement){
        this.condition = condition;
        this.statement = statement;
    }
}
