package com.lemms.api;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


import com.lemms.Canvas.Canvas;
import com.lemms.Tokenizer;
import com.lemms.SyntaxNode.StatementNode;
import com.lemms.interpreter.Interpreter;
import com.lemms.interpreter.StatementVisitor;
import com.lemms.interpreter.object.LemmsData;
import com.lemms.parser.Parser;



public class LemmsAPI {
    private final Map<String, NativeFunction> nativeFunctions = new HashMap<>();
    private List<StatementNode> program;


    public LemmsAPI() {
        // nativeFunctions = new HashMap<>(); //unnötig, direkt im feld initialisierne
        // Besser: die API registriert nur die Funktionen und der Interpreter wird von außen gesetzt. (entkoppelt die API vom Interpreter-Lebenszyklus)
    }

    public void interpret() {
        Interpreter interpreter = new Interpreter(program);
        this.registerCanvasFunctions(interpreter); //Canvas.addPrimitives(this, interpreter);
        interpreter.addNativeFunctions(nativeFunctions);
        interpreter.interpret();
    }

    public Map<String, NativeFunction> getNativeFunctions() {
        // andere Funktionen können hier hinzufügt werden!!1
        return nativeFunctions;
    }

    public void registerFunction(String name, NativeFunction function) {
        nativeFunctions.put(name, function);
    }

    //für Zugriff von außen
    public void registerCanvasFunctions(StatementVisitor visitor) {
        Canvas.addPrimitives(this, visitor);
    }

    public void setScript(String scriptFilePath) {        
        String sourcePath = "src/main/resources/example1.1.lemms"; // String sourcePath = args[0];
        File sourceFile = new File(scriptFilePath);
        
        Tokenizer t = new Tokenizer(sourceFile);
        Parser p = new Parser(t.getTokens());
        program = p.parse();
    }




}
