package com.lemms.SyntaxNode;

import com.lemms.interpreter.StatementVisitor;
import com.lemms.Token;
import java.util.ArrayList;

public class WhileNode extends ConditionedBlock {
    public ValueNode condition;
    public StatementNode statement;
    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitWhileStatement(this);
    }
    public WhileNode(ArrayList<Token> tokens) {
        super(tokens);
    }
}
