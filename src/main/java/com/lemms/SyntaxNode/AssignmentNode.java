package com.lemms.SyntaxNode;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;

public class AssignmentNode extends StatementNode {
    public VariableNode leftHandSide;
    public ExpressionNode rightHandSide;

    public AssignmentNode(VariableNode identifier, ExpressionNode expression) {
        this.leftHandSide = identifier;
        this.rightHandSide = expression;
    }

    @Override
    public FlowSignal accept(StatementVisitor visitor) {
        return visitor.visitAssignmentNode(this);
    }
}
