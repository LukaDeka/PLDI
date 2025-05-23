package com.lemms.SyntaxNode;

import com.lemms.Token;

import java.util.ArrayList;

public class ElifNode extends ConditionedBlock {
    public ElifNode(ArrayList<Token> tokens) {
        super(tokens);
    }
}
