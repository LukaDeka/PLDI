package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;

public class AssignmentNode extends StatementNode {
    public VariableNode leftHandSide;
    public ExpressionNode rightHandSide;
    public final Token equalsToken; //Anker-Token repräsentiert den Node, beinhaltet line

    public AssignmentNode(VariableNode identifier, ExpressionNode expression, Token equalsToken) {
        this.leftHandSide = identifier;
        this.rightHandSide = expression;
        this.equalsToken = equalsToken;
    }

    @Override
    public FlowSignal accept(StatementVisitor visitor) {
        return visitor.visitAssignmentNode(this);
    }
}
