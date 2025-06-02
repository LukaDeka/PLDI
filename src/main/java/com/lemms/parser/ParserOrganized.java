package com.lemms.parser;
import com.lemms.Parser;
import com.lemms.Exceptions.MissingTokenException;
import com.lemms.Exceptions.UnexpectedToken;
import com.lemms.SyntaxNode.*;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.*;

import static com.lemms.TokenType.*;


public class ParserOrganized {
    private static final Logger logger = Logger.getLogger(Parser.class.getName());
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Parser.class);

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                String color = "\u001B[34m";
                String reset = "\u001B[0m";

                switch (record.getLevel().getName()) {
                    case "INFO":
                        color = "\u001B[34m"; // Blau
                        break;
                    case "SEVERE":
                        color = "\u001B[36m"; // Cyan
                        break;
                    default:
                            //color = "\u001B[31m"; // Rot
                        color = "\u001B[33m"; // Gelb für andere Levels
                        //color = "\u001B[32m"; // Grün
                }
                return color + record.getMessage() + reset+ "\n"; // z.B. nur die Nachricht ausgeben
            }

        });
        logger.setUseParentHandlers(false); // verhindert doppelte Logs
        logger.addHandler(handler);
    }


    private final ArrayList<StatementNode> rootNodes = new ArrayList<>();
    private Token current;
    private final List<Token> tokens;

    public ArrayList<StatementNode> getAST() {
        return rootNodes;
    }

    public ParserOrganized(List<Token> tokens) {
        this.tokens = tokens;
    }

        private StatementNode parseStatement() {
        if (match(TokenType.IF)) return parseIfStatement();
        if (match(TokenType.WHILE)) return parseWhileStatement();
        if (match(TokenType.IDENTIFIER) && peek().getType() == TokenType.ASSIGNMENT) {
            return parseAssignment();
        }
        // ... other statement types ...
        throw error("Unexpected token: " + peek());
    }
    
}
