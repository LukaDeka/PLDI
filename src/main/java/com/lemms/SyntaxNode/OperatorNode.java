package com.lemms.SyntaxNode;

import com.lemms.Token.TokenType;
import com.lemms.interpreter.ValueVisitor;

public class OperatorNode extends ValueNode {    
        
    public TokenType operator;
    public ValueNode leftOperand;
    public ValueNode rightOperand;
    
    @Override
    public Object accept(ValueVisitor visitor) {
        return visitor.visitOperatorValue(this);
    }
}
