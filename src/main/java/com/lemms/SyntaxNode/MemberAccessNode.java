package com.lemms.SyntaxNode;

import com.lemms.interpreter.ValueVisitor;
import com.lemms.interpreter.object.LemmsData;

public class MemberAccessNode extends ExpressionNode {
    public ExpressionNode object;
    public String memberName;

    public MemberAccessNode(ExpressionNode object, String memberName) {
        this.object = object;
        this.memberName = memberName;
    }

    @Override
    public LemmsData accept(ValueVisitor visitor) {
        return visitor.visitMemberAccessValue(this);
    }
}