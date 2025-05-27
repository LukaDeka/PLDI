package com.lemms.SyntaxNode;

import com.lemms.Exceptions.SyntaxException;
import com.lemms.Token;
import com.lemms.TokenType;
import com.lemms.interpreter.StatementVisitor;

import java.util.ArrayList;
import java.util.List;

public class AssignmentNode extends StatementNode {
    public VariableNode leftHandSide;
    public ExpressionNode rightHandSide;

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visitAssignmentNode(this);
    }    
    
}
