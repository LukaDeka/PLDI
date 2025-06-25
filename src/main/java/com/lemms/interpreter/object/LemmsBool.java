package com.lemms.interpreter.object;

public class LemmsBool extends LemmsData {
    public boolean value;

    public LemmsBool(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }
}
