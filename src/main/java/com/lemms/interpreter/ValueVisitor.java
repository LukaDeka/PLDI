package com.lemms.interpreter;

import com.lemms.SyntaxNode.*;

public interface ValueVisitor {
    public Object visitVariableValue(VariableNode variableNode);
    public Object visitLiteralValue(LiteralNode literalNode);
    public Object visitOperatorValue(OperatorNode operatorNode);
    public Object visitFunctionCallValue(FunctionCallNode functionNode);    
    
    
}
