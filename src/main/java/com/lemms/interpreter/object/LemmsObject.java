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
    
}
