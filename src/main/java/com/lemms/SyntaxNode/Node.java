package com.lemms.SyntaxNode;

import com.lemms.Token;
import com.lemms.TokenType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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


//
//    @Override
//    public String toString() {
//        return toIndentedString(0);
//    }
//
//    public String toIndentedString(int indent) {
//        StringBuilder result = new StringBuilder();
//        String indentStr = " ".repeat(indent * 5); // z.B. 2 Leerzeichen pro Ebene
//
//        result.append(indentStr).append(this.getClass().getSimpleName()).append(" {\n");
//        Field[] fields = this.getClass().getDeclaredFields();
//
//        for (Field field : fields) {
//            field.setAccessible(true);
//            try {
//                Object value = field.get(this);
//                result.append(indentStr).append("  ").append(field.getName()).append(": ");
//                if (value == null) {
//                    result.append("null\n");
//                } else if (value instanceof List<?> list) {
//                    result.append("[\n");
//                    int i = 0;
//                    for (Object item : list) {
//                        if (item instanceof Node) {
//                            result.append(((Node) item).toIndentedString(indent + 2).stripTrailing());
//                        } else {
//                            result.append(" ".repeat((indent + 2) * 2)).append(item);
//                        }
//                        if (i < list.size() - 1) {
//                            result.append(" ,\n");
//                        } else {
//                            result.append("\n");
//                        }
//                        i++;
//                    }
//                    result.append(indentStr).append("  ]\n");
//
//                } else if (value instanceof Node node) {
//                    result.append("\n").append(node.toIndentedString(indent + 1));
//                } else {
//                    result.append(value).append("\n");
//                }
//            } catch (IllegalAccessException e) {
//                result.append(indentStr).append("  ").append(field.getName()).append(": Zugriff nicht möglich\n");
//            }
//        }
//        result.append(indentStr).append("}\n");
//        return result.toString();
//    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        // Start mit einer leeren Einrückung für das oberste Objekt.
        // Die buildStringRecursive Methode fügt dann den Klassennamen und öffnende Klammer hinzu.
        buildStringRecursive(sb, "", this, true); // true für isTopLevelOrListItem
        return sb.toString();
    }

    // Neuer Parameter: isTopLevelOrListItem - um zu steuern, ob der Klassenname und die Klammern
    // direkt ohne vorherige Einrückung der Zeile geschrieben werden sollen.
    private void buildStringRecursive(StringBuilder sb, String baseIndent, Object obj, boolean isFirstElementInLine) {
        if (obj == null) {
            if (!isFirstElementInLine) sb.append(baseIndent); // Nur einrücken, wenn nicht schon am Zeilenanfang
            sb.append("null\n");
            return;
        }

        // Für primitive Typen, Strings oder bekannte Klassen (wie Token) direkt deren toString() verwenden
        if (obj.getClass().isPrimitive() || obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj instanceof Token) {
            if (!isFirstElementInLine) sb.append(baseIndent);
            sb.append(String.valueOf(obj).replace("\n", "\n" + baseIndent)).append("\n");
            return;
        }

        // Für Listen
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            if (!isFirstElementInLine) sb.append(baseIndent); // Einrückung für die öffnende Klammer der Liste

            if (list.isEmpty()) {
                sb.append("[]\n");
            } else {
                sb.append("[\n");
                String listItemIndent = baseIndent + "  "; // Einrückung für jedes Listenelement
                for (Object item : list) {
                    // Jedes Listenelement startet auf einer neuen Zeile und ist eingerückt.
                    // 'true' für isFirstElementInLine, da buildStringRecursive sich um die Einrückung kümmert.
                    buildStringRecursive(sb, listItemIndent, item, true);
                }
                sb.append(baseIndent).append("]\n"); // Schließende Klammer der Liste
            }
            return;
        }

        // Nur für unsere Node-Subklassen die detaillierte Feldauflistung
        if (!(obj instanceof Node)) {
            if (!isFirstElementInLine) sb.append(baseIndent);
            sb.append(String.valueOf(obj).replace("\n", "\n" + baseIndent)).append("\n");
            return;
        }

        // Für Node-Instanzen: Klassennamen und Felder
        if (isFirstElementInLine) { // Wenn es das erste Element in der Zeile ist (oberste Ebene oder Listenelement)
            sb.append(baseIndent); // Die Basis-Einrückung für den Klassennamen anwenden
        }
        sb.append(obj.getClass().getSimpleName()).append(" {\n");

        String fieldIndent = baseIndent + "  "; // Einrückung für die Felder dieses Nodes

        List<Field> fields = getAllFields(new ArrayList<>(), obj.getClass());
        // Optional: Felder nach Namen sortieren für konsistente Ausgabe
        fields.sort(Comparator.comparing(Field::getName));


        for (Field field : fields) {
            // Bestimmte Felder ignorieren, falls nötig (z.B. interne Felder der Reflection-toString selbst)
            if (field.getName().equals("this$0")) continue; // Internes Feld für innere Klassen

            try {
                field.setAccessible(true);
                Object value = field.get(obj);

                sb.append(fieldIndent).append(field.getName()).append(": ");

                if (value == null) {
                    sb.append("null\n");
                } else if (value instanceof Node || value instanceof List) {
                    // Für Nodes oder Listen eine neue Zeile *vor* dem Wert,
                    // und der rekursive Aufruf kümmert sich um die Einrückung des Werts.
                    sb.append("\n");
                    // 'true' für isFirstElementInLine, da der Wert des Feldes auf einer neuen Zeile beginnt
                    // und seine eigene Einrückung (fieldIndent) erhält.
                    buildStringRecursive(sb, fieldIndent, value, true);
                } else {
                    // Für einfache Werte direkt anhängen, Einrückung wird durch baseIndent der Zeile erledigt.
                    // Keine neue Zeile vor dem Wert, da es auf derselben Zeile wie der Feldname steht.
                    sb.append(String.valueOf(value).replace("\n", "\n" + fieldIndent)).append("\n");
                }
            } catch (IllegalAccessException e) {
                sb.append(fieldIndent).append(field.getName()).append(": <inaccessible>\n");
            }
        }
        sb.append(baseIndent).append("}\n"); // Schließende Klammer des Nodes auf der Basis-Einrückungsebene
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        // Nur Felder hinzufügen, die nicht statisch sind, da statische Felder nicht zum Zustand einer Instanz gehören
        for (Field field : type.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                fields.add(field);
            }
        }

        if (type.getSuperclass() != null && type.getSuperclass() != Node.class && type.getSuperclass() != Object.class) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }





}
