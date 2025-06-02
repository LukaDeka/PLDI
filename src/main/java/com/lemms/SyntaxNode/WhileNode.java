package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;


public class WhileNode extends StatementNode {
    
    public ExpressionNode condition;
    public StatementNode whileBody;

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitWhileStatement(this);
    }
}
