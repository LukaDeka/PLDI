package com.lemms.interpreter;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    private final Environment enclosing;

    // Default constructor for global scope
    public Environment() {
        this.enclosing = null;
    }

    // Constructor for nested scopes (e.g., blocks)
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    private void define(String name, Object value) {
        values.put(name, value);
    }

    public void assign(String name, Object value) {
        if (values.containsKey(name)) {
            values.put(name, value);
        } else if (enclosing != null) {
            enclosing.assign(name, value);
        } else {
            // define(name, value);
        }
    }

    public Object get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        } else if (enclosing != null) {
            return enclosing.get(name);
        } else {
            throw new RuntimeException("Undefined variable '" + name + "'.");
        }
    }
}