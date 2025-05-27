package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;
import com.lemms.Exceptions.SyntaxException;
import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;


public class IfNode extends StatementNode {

    public ExpressionNode condition;
    public StatementNode statement;
    public StatementNode elseStatement;

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitIfStatement(this);
    }

    public void addElseNode(StatementNode elseStatement) {
        if (this.elseStatement == null) {
            this.elseStatement = elseStatement;
        } else {
            throw new SyntaxException("UnexpectedToken: another Else Block already existing");
        }
    }
}
