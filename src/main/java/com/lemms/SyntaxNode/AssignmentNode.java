package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;

public class AssignmentNode extends StatementNode {
    public ExpressionNode leftHandSide;
    public ExpressionNode rightHandSide;

    public AssignmentNode(ExpressionNode identifier, ExpressionNode expression) {
        this.leftHandSide = identifier;
        this.rightHandSide = expression;        
    }

    @Override
    public FlowSignal accept(StatementVisitor visitor) {
        return visitor.visitAssignmentNode(this);
    }
}
