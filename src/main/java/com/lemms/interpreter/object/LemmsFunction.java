package com.lemms.interpreter.object;

import com.lemms.SyntaxNode.FunctionDeclarationNode;
import com.lemms.api.NativeFunction;

public class LemmsFunction extends LemmsData {
    public final FunctionDeclarationNode functionDeclaration;
    public final NativeFunction nativeFunction;
    public final boolean isNative;
    public LemmsFunction(FunctionDeclarationNode value) {
        this.functionDeclaration = value;
        this.nativeFunction = null;
        isNative = false;
    }

        public LemmsFunction(NativeFunction value) {
        this.functionDeclaration = null;
        this.nativeFunction = value;
        isNative = true;
    }

        @Override
        public String toString() {
            return isNative ? "NativeFunction" : functionDeclaration.functionName;
        }
}
