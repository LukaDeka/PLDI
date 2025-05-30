package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.StatementVisitor;
import com.lemms.interpreter.ValueVisitor;

public class FunctionCallStatementNode extends StatementNode {

    public FunctionCallNode functionCall;
    
    @Override
    public void accept(StatementVisitor visitor) {        
        visitor.visitFunctionCallStatement(this);
    }

}
