package com.lemms.Exceptions;

// package com.lemms; // Oder wo auch immer ihr es haben wollt

import com.lemms.Token;


//checked exception?
public class LemmsParseError extends RuntimeException {
    final Token token;

    public LemmsParseError(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

}