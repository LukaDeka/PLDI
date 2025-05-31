package com.lemms.SyntaxNode;

import com.lemms.Token;

import java.util.ArrayList;

public class WhileNode extends ConditionedBlock {
    public WhileNode(ArrayList<Token> tokens) {
        super(tokens);
    }

    @Override
    public String toString() {
        return super.toString() + "condition:"+condition + "then"+thenBlock;
    }
}
