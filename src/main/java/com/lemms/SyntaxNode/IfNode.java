package com.lemms.SyntaxNode;

import com.lemms.Exceptions.SyntaxException;
import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;


public class IfNode extends ConditionedBlock {

    private ExpressionNode condition;
    private StatementNode thenBlock;
    private List<ElifNode> elifNodes = new ArrayList<>();
    private ElseNode elseNode;


    public IfNode(ArrayList<Token> tokens) {
        super(tokens);
    }

    public void addElif(ElifNode elifNode) {
        this.elifNodes.add(elifNode);
    }

    public void addElseNode(ElseNode elseNode) {
        if (this.elseNode == null) {
            this.elseNode = elseNode;
        } else {
            throw new SyntaxException("UnexpectedToken: another Else Block already existing");
        }
    }
}
