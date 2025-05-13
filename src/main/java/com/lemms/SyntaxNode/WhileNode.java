package com.lemms.SyntaxNode;

public class WhileNode extends StatementNode {
    public ValueNode condition;
    public StatementNode statement;

    public void VisitNode(Visitor visitor) {
        visitor.visitWhileNode(this)   ;
    }
}
