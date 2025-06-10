package com.lemms.interpreter;

import java.util.HashMap;
import java.util.Map;

import com.lemms.api.NativeFunction;

public class PredefinedFunctionLibrary {

    public static Map<String, NativeFunction> getPredefinedFunctions() {
        Map<String, NativeFunction> functions = new HashMap<>();

        functions.put("print", args -> {
            System.out.println(args.get(0));
            return null;
        });
        
        functions.put("len", args -> {
            if (args.get(0) instanceof String) {
                return ((String) args.get(0)).length();
            } 
            throw new RuntimeException("Unsupported type for len function.");
        });

        functions.put("exit", args -> {
            Object arg1 = args.get(0);
            if (arg1 instanceof Integer) {
                System.exit((Integer) arg1);
            } else {
                System.exit(0);
            }
            return null;
        });

        return functions;
    }     
}