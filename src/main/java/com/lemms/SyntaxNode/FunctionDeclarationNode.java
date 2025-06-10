package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.StatementVisitor;


public class FunctionDeclarationNode extends StatementNode {
    
    public String functionName;
    public List<String> paramNames;
    public StatementNode functionBody;

    @Override
    public void accept(StatementVisitor visitor) {
        // visitor.visitWhileStatement(this);
    }
}
