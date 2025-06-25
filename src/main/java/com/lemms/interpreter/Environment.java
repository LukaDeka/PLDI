
package com.lemms.interpreter;
import java.util.HashMap;
import java.util.Map;

import com.lemms.interpreter.object.LemmsData;

public class Environment {
    private final Map<String, LemmsData> values = new HashMap<>();
    public final Environment enclosing;

    // Default constructor for global scope
    public Environment() {
        this.enclosing = null;
    }

    // Constructor for nested scopes (e.g., blocks)
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public Map<String, LemmsData> getValues() {
        return values;
    }

    private Environment findVariableEnv(String name) {
        if (values.containsKey(name)) {
            return this;
        } else if (enclosing != null) {
            Environment envFound = enclosing.findVariableEnv(name);
            return envFound;
        } else {
            return null;
        }
    }

    public void assign(String name, LemmsData value) {
        Environment env = findVariableEnv(name);
        
        if (env == null) {
            values.put(name, value);
        } else {
            env.values.put(name, value);
        }
    }

    public LemmsData get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        } else if (enclosing != null) {
            return enclosing.get(name);
        } else {
            //throw new RuntimeException("Undefined variable '" + name + "'.");
                //PROBLEMATIC: Environment doesn't know the Token/Node -> doesn't know the LINE
                //instead i returrn and throw the Exception inside the Interpreter
            return null;
        }
    }
}