package com.lemms.interpreter.object;

public class LemmsInt extends LemmsData {
    public final int value;    
    public LemmsInt(int value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
}
