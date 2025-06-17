package com.lemms.SyntaxNode;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;
import com.sun.jdi.Field;

public class ClassDeclarationNode extends StatementNode{

    public String className;
    public Field localVariables;

    public FlowSignal accept(StatementVisitor visitor) {
        visitor.visitClassDeclarationStatement(this);
        return null; // Function declarations do not return a FlowSignal
    }
}
