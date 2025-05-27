package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.ValueVisitor;

public class FunctionNode extends ExpressionNode {

    public String functionName;
    public List<ExpressionNode> params;
    
    @Override
    public Object accept(ValueVisitor visitor) {        
        return visitor.visitFunctionValue(this);
    }

}
