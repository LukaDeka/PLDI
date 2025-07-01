package com.lemms.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;


import com.lemms.Canvas.Canvas;
import com.lemms.Tokenizer;
import com.lemms.SyntaxNode.StatementNode;
import com.lemms.interpreter.Interpreter;
import com.lemms.interpreter.object.LemmsData;
import com.lemms.parser.Parser;

public class LemmsAPI {
    private HashMap<String, NativeFunction> nativeFunctions;
    private List<StatementNode> program = new ArrayList<>();
    public void registerFunction(String name, Function<List<LemmsData>, LemmsData> function) {
        nativeFunctions.put(name, function::apply);
    }

    public void setScript(String scriptFilePath) {        
        String sourcePath = "src/main/resources/example1.1.lemms"; // String sourcePath = args[0];
        File sourceFile = new File(scriptFilePath);
        
        Tokenizer t = new Tokenizer(sourceFile);
        Parser p = new Parser(t.getTokens());
        program = p.parse();
    }

    public void interpret() {
        Interpreter interpreter = new Interpreter(program);
        setupInterpreter(interpreter);
        interpreter.interpret();
    }

    private void setupInterpreter(Interpreter interpreter) {
        Canvas.addPrimitives(this, interpreter);
        interpreter.addNativeFunctions(this.nativeFunctions);
    }

    public Interpreter createInterpreterForRepl() {
        Interpreter interpreter = new Interpreter(new ArrayList<>());
        setupInterpreter(interpreter);
        interpreter.initializeGlobalScope();

        return interpreter;
    }


    public LemmsAPI() {
        nativeFunctions = new HashMap<>();
    }
}
