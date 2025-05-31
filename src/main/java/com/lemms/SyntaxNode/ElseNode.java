package com.lemms.SyntaxNode;

import com.lemms.Parser;
import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;

import static com.lemms.TokenType.*;

public class ElseNode extends BlockNode{
    public ElseNode(List<? extends StatementNode> statements) {
        super(statements);
    }
}
