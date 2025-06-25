package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;
import com.sun.jdi.Field;

public class ClassDeclarationNode extends StatementNode{

    public String className;
    // list of the names of the local variables in this class
    public List<String> localVariables;
    public List<FunctionDeclarationNode> localFunctions;

    public FlowSignal accept(StatementVisitor visitor) {
        visitor.visitClassDeclarationStatement(this);
        return null; // Class declarations do not return a FlowSignal
    }
}
