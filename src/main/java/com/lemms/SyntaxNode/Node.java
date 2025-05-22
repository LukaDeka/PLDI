package com.lemms.SyntaxNode;

import com.lemms.Exceptions.SyntaxException;
import com.lemms.Token;
import com.lemms.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public abstract class Node {
    //ArrayList<Node> children;



    static void checkTokenList(List<Token> tokens, IntPredicate sizeCondition, TokenType... expectedTypes) {
        List<String> errors = new ArrayList<>();

        if (!sizeCondition.test(tokens.size())) {
            errors.add("Error: Incorrect number of tokens. Expected: " + expectedTypes.length + ", received: " + tokens.size());
        }

        // Überprüfung der angegebenen erwarteten Token-Typen
        for (int i = 0; i < expectedTypes.length; i++) {
            if (i >= tokens.size() || tokens.get(i).getType() != expectedTypes[i]) {
                errors.add("Error: Token at position " + i + " does not match the expected type: " + expectedTypes[i]);
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
    }

}
