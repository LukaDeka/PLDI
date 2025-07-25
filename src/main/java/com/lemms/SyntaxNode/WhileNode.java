package com.lemms.SyntaxNode;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;

public class WhileNode extends StatementNode {

    public ExpressionNode condition;
    public StatementNode whileBody;

    @Override
    public FlowSignal accept(StatementVisitor visitor) {
        return visitor.visitWhileStatement(this);
    }
}
