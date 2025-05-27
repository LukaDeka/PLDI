package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;
import com.lemms.Exceptions.SyntaxException;
import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;


public class IfNode extends ConditionedBlock {

    public ExpressionNode condition;
    public StatementNode statement;
    private List<ElifNode> elifNodes = new ArrayList<>();
    public StatementNode elseStatement;

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitIfStatement(this);
    }

    public IfNode(ArrayList<Token> tokens) {
        super(tokens);
    }

    public void addElif(ElifNode elifNode) {
        this.elifNodes.add(elifNode);
    }

    public void addElseNode(StatementNode elseStatement) {
        if (this.elseStatement == null) {
            this.elseStatement = elseStatement;
        } else {
            throw new SyntaxException("UnexpectedToken: another Else Block already existing");
        }
    }
}
