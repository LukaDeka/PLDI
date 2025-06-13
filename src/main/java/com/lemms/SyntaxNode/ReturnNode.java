package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;
import com.lemms.interpreter.ValueVisitor;

public class ReturnNode extends StatementNode {
    
    public ExpressionNode value;

    @Override
    public FlowSignal accept(StatementVisitor visitor) {
        return visitor.visitReturnNode(this);
    }
}
