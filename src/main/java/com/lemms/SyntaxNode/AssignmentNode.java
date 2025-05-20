package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;

public class AssignmentNode extends StatementNode {
    VariableNode leftHandSide;
    ValueNode rightHandSide;
    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitAssignmentNode(this);
    }
}
