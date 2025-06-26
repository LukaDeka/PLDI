package com.lemms.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.lemms.api.NativeFunction;
import com.lemms.interpreter.object.LemmsInt;
import com.lemms.interpreter.object.LemmsString;

public class PredefinedFunctionLibrary {

    public static Map<String, NativeFunction> getPredefinedFunctions() {
        Map<String, NativeFunction> functions = new HashMap<>();

        functions.put("println", args -> {
            System.out.println(args.get(0));
            return null;
        });

        functions.put("print", args -> {
            System.out.print(args.get(0));
            return null;
        });

        functions.put("len", args -> {
            if (args.get(0) instanceof LemmsString) {
                int length = ((LemmsString)args.get(0)).value.length();
                return new LemmsInt(length);
            } 
            throw new RuntimeException("Unsupported type for len function.");
        });

        functions.put("exit", args -> {
            if (!args.isEmpty() && args.get(0) instanceof LemmsInt) {

                System.exit(((LemmsInt) args.get(0)).value);
            } else {
                System.exit(0);
            }
            return null;
        });

        functions.put("randint", args -> {
            Random rand = new Random();

            if (args.isEmpty()) {
                return new LemmsInt(rand.nextInt());
            }
            if (args.get(0) instanceof LemmsInt bound) {
                return new LemmsInt(rand.nextInt(bound.value));
            }
            return null;
        });

        return functions;
    }     
}