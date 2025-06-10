package com.lemms.SyntaxNode;

import com.lemms.interpreter.FlowSignal;
import com.lemms.interpreter.StatementVisitor;
import com.lemms.Exceptions.SyntaxException;

import java.util.ArrayList;
import java.util.List;


public class IfNode extends StatementNode {

    public ExpressionNode condition;
    public StatementNode ifBody;
    public StatementNode elseStatement;

    @Override
    public FlowSignal accept(StatementVisitor visitor) {
        return visitor.visitIfStatement(this);
    }

}
