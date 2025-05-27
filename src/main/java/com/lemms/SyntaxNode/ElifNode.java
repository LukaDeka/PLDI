package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.interpreter.StatementVisitor;

import java.util.ArrayList;

public class ElifNode extends ConditionedBlock {
    public ElifNode(ArrayList<Token> tokens) {
        super(tokens);
    }

    @Override
    public void accept(StatementVisitor visitor) {

    }
}

