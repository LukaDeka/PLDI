package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;
import com.lemms.Exceptions.SyntaxException;

import java.util.ArrayList;
import java.util.List;


public class IfNode extends ConditionedBlock {

//    private ExpressionNode condition;
//    private StatementNode thenBlock;
    public List<ElifNode> elifNodes = new ArrayList<>();
    public ElseNode elseStatement;

    public IfNode(ExpressionNode condition, StatementNode thenBlock) {
        super(condition, thenBlock);
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitIfStatement(this);
    }


    public void addElif(ElifNode elifNode) {
        this.elifNodes.add(elifNode);
    }

    public void addElseNode(ElseNode elseNode) {
        if (this.elseStatement == null) {
            this.elseStatement = elseNode;
        } else {
            throw new SyntaxException("UnexpectedToken: another Else Block already existing");
        }
    }



//    @Override
//    public String toString() {
//        String result = "IfNode { \ncondition: " + condition +",\nelifNodes= " + elifNodes + ",\nelseNode=" + elseNode + "}";
//
//        return result;
//    }
}
