package com.lemms.Exceptions;

public class MissingTokenException extends TokenException {
    public MissingTokenException(String message) {
        super("Missing token: " + message);
    }
}
