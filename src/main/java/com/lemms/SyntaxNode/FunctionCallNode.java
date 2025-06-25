package com.lemms.SyntaxNode;

import java.util.List;

import com.lemms.interpreter.ValueVisitor;
import com.lemms.interpreter.object.LemmsData;

public class FunctionCallNode extends ExpressionNode {

    public String functionName;
    public List<ExpressionNode> params;
    
    @Override
    public LemmsData accept(ValueVisitor visitor) {        
        return visitor.visitFunctionCallValue(this);
    }

}
