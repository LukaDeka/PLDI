package com.lemms.interpreter;

import com.lemms.SyntaxNode.*;
import com.lemms.interpreter.object.LemmsData;

public interface ValueVisitor {
    public LemmsData visitVariableValue(VariableNode variableNode);
    public LemmsData visitLiteralValue(LiteralNode literalNode);
    public LemmsData visitOperatorValue(OperatorNode operatorNode);
    public LemmsData visitFunctionCallValue(FunctionCallNode functionNode);    
    
    
}
