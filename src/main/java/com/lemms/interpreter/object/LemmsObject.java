package com.lemms.interpreter.object;

import java.util.Map;

import com.lemms.SyntaxNode.ClassDeclarationNode;
import com.lemms.interpreter.Environment;

public class LemmsObject extends LemmsData {
    public final Environment environment;
    public final ClassDeclarationNode classDeclaration;

    public LemmsObject(ClassDeclarationNode classDeclaration, Environment globalEnvironment) {
        this.classDeclaration = classDeclaration;
        this.environment = new Environment(globalEnvironment);
    }

    public LemmsData get(String name) {
        return environment.get(name);
    }

    public void set(String name, LemmsData value) {
        environment.assign(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(classDeclaration.className).append("{\n");
        for (Map.Entry<String, LemmsData> entry : environment.getValues().entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    
}
