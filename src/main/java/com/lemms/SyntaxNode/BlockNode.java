package com.lemms.SyntaxNode;

import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;

import static com.lemms.TokenType.*;
import com.lemms.interpreter.StatementVisitor;

public class BlockNode extends StatementNode {
    public List<? extends StatementNode> statements;

    public BlockNode(List<? extends StatementNode> statements) {
        this.statements = statements;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitBlockStatement(this);
    }
}
