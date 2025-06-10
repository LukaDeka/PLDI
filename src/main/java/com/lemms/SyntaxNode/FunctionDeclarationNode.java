package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;


public class FunctionDeclarationNode extends StatementNode {
    
    public String functionName;
    public List<String> paramNames;
    public StatementNode functionBody;

    @Override
    public FlowSignal accept(StatementVisitor visitor) {
        visitor.visitFunctionDeclarationStatement(this);
        return null; // Function declarations do not return a FlowSignal
    }
}
