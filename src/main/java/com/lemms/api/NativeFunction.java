package com.lemms.api;

import java.util.List;

import com.lemms.interpreter.object.LemmsData;

@FunctionalInterface
public interface NativeFunction {
    /**
     * @param args   The list of arguments passed from the script. 
     *               You can convention‐define them as e.g. List<Object> or List<Value> if you have a Value class.
     * @return       A result object (or null) to return back into the script.
     * @throws RuntimeException if something goes wrong in the host side.
     */
    LemmsData apply(List<LemmsData> args);
}