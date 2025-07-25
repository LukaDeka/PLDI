package com.lemms.Exceptions;

import com.lemms.Token;

public class LemmsRuntimeException extends RuntimeException {
    final Token token;

    public LemmsRuntimeException(Token token, String message) {
        super(message);
        this.token = token;
    }

    public LemmsRuntimeException(String message) {
        super(message);        
        token = null;
    }

    public Token getToken() {
        return token;
    }
}