package com.lemms.SyntaxNode;

import com.lemms.Parser;
import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;

import static com.lemms.TokenType.*;

public class BlockNode extends StatementNode {
    List<? extends StatementNode> statements;

    public BlockNode(List<? extends StatementNode> statements) {
        this.statements = statements;
    }
}
