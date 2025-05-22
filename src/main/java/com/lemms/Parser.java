package com.lemms;
import com.lemms.Exceptions.MissingTokenException;
import com.lemms.SyntaxNode.AssignmentNode;
import com.lemms.SyntaxNode.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class.getName());
    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                String blue = "\u001B[34m";
                String reset = "\u001B[0m";
                return blue + record.getMessage() + reset+ "\n"; // z.B. nur die Nachricht ausgeben
            }
        });
        logger.setUseParentHandlers(false); // verhindert doppelte Logs
        logger.addHandler(handler);
    }

    private final ArrayList<Node> rootNodes = new ArrayList<>();
    private Token current;
    private Iterator<Token> iterator = null;

    public Parser(ArrayList<Token> fileTokens) {
        System.out.println(fileTokens);
        logger.info("BEGIN OF PARSER");
        iterator = fileTokens.iterator(); //Iterator is like a HEAD or POINTER going through the Tokens, starting from Token the very first Token

        // first Iteration = Top Level Statements (and Blocks)
        // split based on semicolons (and block-braces)

        try {
            getStatements(fileTokens);  //Iterator<Token> iterator = tokens.iterator();
        } catch (MissingTokenException e) {
            logger.warning(e.getMessage());
        }

        logger.info("END OF PARSER");
    }

    public void getStatements(ArrayList<Token> tokens) throws MissingTokenException {
        //while (iterator.hasNext()) {
            current = iterator.next();

            //different handling depending on type of top-level-statement (assigment, block, loop, if-else)
            switch (current.getType()) {
                case WHILE, IF, ELSE -> {
                    ArrayList<Token> result = addAllTokensUntil(TokenType.CURLY_CLOSED); //what about nested blocks??
                    //toDO:
                    //create...
                    //rootNodes.add();
                    logger.info(result.toString());
                }
                case CURLY_OPEN -> {
                    ArrayList<Token> result = addAllTokensUntil(TokenType.CURLY_CLOSED);
                    //toDO:
                    ArrayList<Token> blockNodes = addAllTokensUntil(TokenType.CURLY_OPEN); //ToDo: implementing logic to ignore other nested blocks! in addAllTokensUntil
                    AssignmentNode node = new AssignmentNode(blockNodes);
                    rootNodes.add(node);
                    logger.info(blockNodes.toString());

                    //create...
                    //rootNodes.add();
                    logger.info(result.toString());
                }
                default -> {
                    ArrayList<Token> assignmentNodes = addAllTokensUntil(TokenType.SEMICOLON);
                    AssignmentNode node = new AssignmentNode(assignmentNodes);
                    rootNodes.add(node);
                    logger.info(assignmentNodes.toString());
                }
            }
            logger.fine(current.toString());
//        }
    }


    /**
     * collect and return all Tokens from current position up to the first occurrence "delimiterToken"
     * @param delimiterToken stops the collecting
     * @return List of collected Tokens
     */

    private ArrayList<Token> addAllTokensUntil(TokenType delimiterToken) throws MissingTokenException {
        ArrayList<Token> result = new ArrayList<>();
        while (current.getType()!= delimiterToken){
            result.add(current);
            if (iterator.hasNext()) current = iterator.next();
            else throw new MissingTokenException(delimiterToken.toString());
        }
        result.add(current); //one more add for closing element
        return result;
    }


    public ArrayList<Node> getAST() {
        return rootNodes;
    }

    public static void main(String[] args) {

        //ich hab eine manuelle TokenListe erstellt zum Testen des Codes
        //(Lukas Tokenizer funktioniert noch nicht ganz)
        //der folgende Code ist aber an sich nicht relevant
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token("funny_var1", 1, TokenType.IDENTIFIER));
        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
        tokens.add(new Token("100100134", 1, TokenType.INT));
        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token("funny_var", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
//        tokens.add(new Token("\"epic string\\\"string\"", 1, TokenType.STRING));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token(null, 1, TokenType.IF));
//        tokens.add(new Token(null, 1, TokenType.BRACKET_OPEN));
//        tokens.add(new Token("funny_var2", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.NEQ));
//        tokens.add(new Token("100100134", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.BRACKET_CLOSED));
//        tokens.add(new Token(null, 1, TokenType.CURLY_OPEN));
//        tokens.add(new Token("print", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.BRACKET_OPEN));
//        tokens.add(new Token("\"awman\"", 1, TokenType.STRING));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token(null, 1, TokenType.CURLY_CLOSED));
//        tokens.add(new Token("3", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.LEQ));
//        tokens.add(new Token("4", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token("whiletrue", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
//        tokens.add(new Token("true", 1, TokenType.BOOL));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token("whilefalse", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
//        tokens.add(new Token("false", 1, TokenType.BOOL));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token("string____test_____", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
//        tokens.add(new Token("\"\"", 1, TokenType.STRING));

        new Parser(tokens);
    }




    //    int currentLine = 0;
//        ArrayList<Token> line_tokens ;
//
//        while (true){
//            line_tokens = getOneLine(fileTokens, currentLine);
//            currentLine++;
//            if (line_tokens == null) break;
//
//            if (line_tokens.isEmpty()) continue; // here an empty tree should be added
//
//            //System.out.println(line_tokens);
//
//            parseLine();
//        }

    //    private ArrayList<Token> getOneLine(ArrayList<Token> fileTokens, int currentLine){
//        ArrayList<Token> line = new ArrayList<>();
//
//        for (Token token : fileTokens){
//            // break condition for tokens beyond the current line
//            if (token.getLine() > currentLine){
//                return line;
//            }
//
//            if (token.getLine() == currentLine){
//                line.add(token);
//            }
//        }
//
//        // "last line in file" case
//        if (line.isEmpty()) return null;
//        else return line;
//    }
//
//    private void parseLine(){
//        //tryAssignmentParse()
//        //tryStatementParse
//        //tryWhileParse
//        //tryIfParse
//    }

}
