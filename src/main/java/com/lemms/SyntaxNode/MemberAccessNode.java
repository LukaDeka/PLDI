package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;
import com.lemms.interpreter.object.LemmsData;

public class MemberAccessNode extends ExpressionNode {

    public ExpressionNode object; // The base object (could be another MemberAccessNode, FunctionCallNode,
                                  // VariableNode, etc.)
    public MemberAccessNode child; // The next access/call in the chain, or null

    public MemberAccessNode(ExpressionNode object, MemberAccessNode child) {
        this.object = object;
        this.child = child;
    }

    @Override
    public LemmsData accept(ValueVisitor visitor) {
        return visitor.visitMemberAccessValue(this);
    }
}