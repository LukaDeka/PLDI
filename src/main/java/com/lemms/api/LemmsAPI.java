package com.lemms.api;

import java.lang.annotation.Native;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

import com.lemms.SyntaxNode.StatementNode;
import com.lemms.interpreter.Interpreter;

public class LemmsAPI {
    private HashMap<String, NativeFunction> nativeFunctions;

    public void registerFunction(String name, Function<List<Object>, Object> function) {
        nativeFunctions.put(name, function::apply);

    }
    
    public void setScript(String scriptFilePath) {

    }

    public void interpret() {
        List<StatementNode> program = null;
        Interpreter interpreter = new Interpreter(program, nativeFunctions);
        interpreter.interpret();
    }
    public LemmsAPI() {
        nativeFunctions = new HashMap<>();
    }
}

