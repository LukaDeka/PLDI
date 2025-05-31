package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.TokenType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public abstract class Node {
    protected static final Logger logger = Logger.getLogger(Node.class.getName());
    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                String cyan = "\u001B[36m";
                String reset = "\u001B[0m";
                return cyan + record.getMessage() + reset+ "\n"; // z.B. nur die Nachricht ausgeben
            }
        });
        logger.setUseParentHandlers(false); // verhindert doppelte Logs
        logger.addHandler(handler);
    }

    //Helper function
    static void checkTokenList(List<Token> tokens, IntPredicate sizeCondition, TokenType... expectedTypes) {
        List<String> errors = new ArrayList<>();

        if (!sizeCondition.test(tokens.size())) {
            errors.add(String.format("Error: Incorrect number of tokens. Expected: %s or more, received: %s", expectedTypes.length, tokens.size()));
            errors.add("Error: Incorrect number of tokens. Expected: " + expectedTypes.length + " or more, received: " + tokens.size());
        }

//        // Überprüfung der angegebenen erwarteten Token-Typen
//        for (int i = 0; i < expectedTypes.length; i++) {
//            if (i >= tokens.size() || tokens.get(i).getType() != expectedTypes[i]) {
//                errors.add(String.format("Error: Token at position %s [%s] does not match the expected type: %s", i, tokens.get(i).toString2(),expectedTypes[i]));
//            }
//        }

        // Überprüfung der angegebenen erwarteten Token-Typen
         //tokenCounter
        int j = 0;
        int i = 0;
        while(i < expectedTypes.length) {
            //default: normal Tokens
            if (expectedTypes[i] != null) {
                //mismatch
                if (i >= tokens.size() || tokens.get(j).getType() != expectedTypes[i]) {
                    errors.add(String.format("Error: Token at position %s [%s] does not match the expected type: %s", j, tokens.get(j).toString2(), expectedTypes[i]));
                    break;
                }
                //match
                i++;
                j++;

            //else: "null" (filler-Token)
            } else {
                j++; //increment here statt weiter unten, weil gerantiert dass mind 1element fillt
                if (i >= tokens.size()) {
                    errors.add(String.format("Error: Token at position %s [%s] does not match the expected type: %s", j, tokens.get(j).toString2(), expectedTypes[i + 1]));
                } else {
                    if (j >= tokens.size()) {
                        //errors.add(String.format("Error: Token at position %s [%s] does not match the expected type: %s", j, tokens.get(j).toString2(), expectedTypes[i + 1]));
                        errors.add(String.format("Error: Token of expected type: %s not found, i=%s, j=%s" , expectedTypes[i + 1],i,j));
                        break;
                    }
                    if (tokens.get(j).getType() != expectedTypes[i + 1]) {
                       //skip until expectedType found //kein j++ here, weil bereits zu beginn ein increment

                    } else if (tokens.get(j).getType() == expectedTypes[i + 1]) {
                        //correct token found
                        j++;
                        i++;
                        i++; //2x because j is one ahead
                    } else {
                        //sollte nicht eintreten, zum testen
                        System.out.println("AAAAAAAAAAAAAA");
                    }
                }
            }
        }


        if (!errors.isEmpty()) {
            errors.add(tokens.toString());
            throw new IllegalArgumentException("\n "+String.join(";\n ", errors));
        }
    }



    @Override
    public String toString() {
        return toIndentedString(0);
    }

    public String toIndentedString(int indent) {
        StringBuilder result = new StringBuilder();
        String indentStr = " ".repeat(indent * 5); // z.B. 2 Leerzeichen pro Ebene

        result.append(indentStr).append(this.getClass().getSimpleName()).append(" {\n");
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                result.append(indentStr).append("  ").append(field.getName()).append(": ");
                if (value == null) {
                    result.append("null\n");
                } else if (value instanceof List<?> list) {
                    result.append("[\n");
                    int i = 0;
                    for (Object item : list) {
                        if (item instanceof Node) {
                            result.append(((Node) item).toIndentedString(indent + 2).stripTrailing());
                        } else {
                            result.append(" ".repeat((indent + 2) * 2)).append(item);
                        }
                        if (i < list.size() - 1) {
                            result.append(" ,\n");
                        } else {
                            result.append("\n");
                        }
                        i++;
                    }
                    result.append(indentStr).append("  ]\n");

                } else if (value instanceof Node node) {
                    result.append("\n").append(node.toIndentedString(indent + 1));
                } else {
                    result.append(value).append("\n");
                }
            } catch (IllegalAccessException e) {
                result.append(indentStr).append("  ").append(field.getName()).append(": Zugriff nicht möglich\n");
            }
        }
        result.append(indentStr).append("}\n");
        return result.toString();
    }



}
