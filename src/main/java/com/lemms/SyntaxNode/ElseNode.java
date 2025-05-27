package com.lemms.SyntaxNode;

import com.lemms.Parser;
import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;

import static com.lemms.TokenType.*;

public class ElseNode extends BlockNode{
    public ElseNode(ArrayList<Token> tokens) {
        super(
                new ArrayList<>(tokens.subList(1,tokens.size())),
                tokens.get(0).getType().toString()
        );
    }
}
