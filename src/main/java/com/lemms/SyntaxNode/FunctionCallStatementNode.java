package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;
import com.lemms.interpreter.ValueVisitor;

public class FunctionCallStatementNode extends StatementNode {

    public FunctionCallNode functionCall;
    public ExpressionNode expression;
    
    @Override
    public FlowSignal accept(StatementVisitor visitor) {        
        return visitor.visitFunctionCallStatement(this);
    }

}
