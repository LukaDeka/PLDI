package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.ValueVisitor;

public class FunctionCallNode extends ExpressionNode {

    public String functionName;
    public List<ExpressionNode> params;

    public String printValue;
    
    @Override
    public Object accept(ValueVisitor visitor) {        
        return visitor.visitFunctionCallValue(this);
    }

}
