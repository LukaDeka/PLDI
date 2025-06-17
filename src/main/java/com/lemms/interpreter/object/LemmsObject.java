package com.lemms.interpreter.object;

import java.util.Map;

import com.lemms.SyntaxNode.ClassDeclarationNode;

public class LemmsObject extends LemmsData {
    private final Map<String, LemmsData> properties;
    public final ClassDeclarationNode classDeclaration;

    public LemmsObject(ClassDeclarationNode classDeclaration, Map<String, LemmsData> properties) {
        this.classDeclaration = classDeclaration;
        this.properties = properties;
    }

    public LemmsData get(String name) {
        return properties.get(name);
    }

    public void set(String name, LemmsData value) {
        properties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(classDeclaration.className).append("{\n");
        for (Map.Entry<String, LemmsData> entry : properties.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    
}
