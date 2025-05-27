package com.lemms;
import com.lemms.SyntaxNode.BlockNode;
import com.lemms.SyntaxNode.Node;

import com.lemms.SyntaxNode.StatementNode;
import com.lemms.Exceptions.MissingTokenException;
import com.lemms.Exceptions.UnexpectedToken;
import com.lemms.SyntaxNode.*;

import java.util.ArrayList;
import java.util.Iterator;
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

    private final ArrayList<StatementNode> rootNodes = new ArrayList<>();
    private Token current;
    private final Iterator<Token> iterator;

    public ArrayList<StatementNode> getAST() {
        return rootNodes;
    }

    public Parser(ArrayList<Token> tokens) {
        iterator = tokens.iterator(); //Iterator is like a HEAD or POINTER going through the Tokens, starting from Token the very first Token
    }

    public void parseStatements() throws MissingTokenException {
        logger.info("BEGIN OF PARSER");

        // first Iteration = Top Level Statements (and Blocks)
        // split based on semicolons (and block-braces)
        try {
            while (iterator.hasNext()) {
                current = iterator.next();

                //different handling depending on type of top-level-statement (assigment, block, loop, if-else)
                switch (current.getType()) {
                    case IF -> {
                        ArrayList<Token> ifTokens = addAllTokensUntil(TokenType.BRACES_CLOSED); //what about nested blocks??
                        logger.info(ifTokens + "\n----- CREATE IF BLOCK -----");

                        // TODO create the ifnode
                        rootNodes.add(new IfNode());
                    }
                    

                    case ELSE -> {
                        ArrayList<Token> elseToken = addAllTokensUntil(TokenType.BRACES_CLOSED); //what about nested blocks??
                        Node lastNode = rootNodes.get(rootNodes.size() -1 );
                        if (lastNode instanceof IfNode ifNode) {
                            logger.info(elseToken + "\n----- CREATE ELSE BLOCK -----");

                            // TODO : create ElseNode
                            StatementNode elseNode = null;

                            ifNode.addElseNode(elseNode);
                        } else {
                            throw new UnexpectedToken("ElseNode must have corresponding IfNode");
                        }
                    }

                    case WHILE -> {
                        ArrayList<Token> whileTokens = addAllTokensUntil(TokenType.BRACES_CLOSED); //what about nested blocks??
                        logger.info(whileTokens + "\n----- CREATE WHILE BLOCK -----");
                        // todo create the whilenode
                        rootNodes.add(new WhileNode());
                    }

                    case BRACES_OPEN -> {
                        ArrayList<Token> blockTokens = addAllTokensUntil(TokenType.BRACES_CLOSED); //ToDo: implementing logic to ignore other nested blocks! in addAllTokensUntil
                        logger.info(blockTokens + "\n----- CREATE BLOCK -----");

                        // TODO create the block node
                        rootNodes.add(new BlockNode());
                    }

                    default -> {
                        ArrayList<Token> assignmentNodes = addAllTokensUntil(TokenType.SEMICOLON);

                        if (assignmentNodes.size() <= 1) { //ignore for empty statements like ";;;" | .empty() wenn ohne SEMICOLON Token
                            logger.info("----- IGNORE EMPTY STATEMENT -----");
                            continue;
                        }
                        logger.info(assignmentNodes + "\n----- CREATE ASSIGNMENT NODE -----");

                        // TODO create the assignment node
                        rootNodes.add(new AssignmentNode());

                    }

                }
            }

        } catch (MissingTokenException e) {
            logger.warning(e.getMessage());
        } finally {
            logger.info("END OF PARSER");

        }
    }


    public ArrayList<Token> addAllTokensUntil(TokenType delimiterToken) throws MissingTokenException {
        TokenType nestedToken;
        switch (delimiterToken) {
            case SEMICOLON -> nestedToken = null; //Semikolon benötigt keine verschachtelte Verarbeitung
            case BRACES_CLOSED -> nestedToken = TokenType.BRACES_OPEN;
            case BRACKET_CLOSED -> nestedToken = TokenType.BRACKET_OPEN;
            default -> nestedToken = null;
        }


        return addAllTokensUntil(delimiterToken, nestedToken);
    }


    public ArrayList<Token> addAllTokensUntil(TokenType delimiterToken, TokenType nestedToken) throws MissingTokenException {
        ArrayList<Token> result = new ArrayList<>();
        int nestedCounter = -1;

        //null check
        if (current == null) {
            if (iterator.hasNext()) {
                current = iterator.next();
            } else {
                throw new MissingTokenException("Keine Tokens vorhanden.");
            }
        }

        //stop when current is a DELIMITER TOKEN && there are NO NESTED blocks
        while (current.getType()!= delimiterToken || nestedCounter > 0){
            if (current.getType() == delimiterToken) nestedCounter--;   //current is delimiter (but nested)
            else if (current.getType() == nestedToken) nestedCounter++; //current is nested (begin)
            result.add(current);
           // logger.info(current.toString());
            if (iterator.hasNext()) current = iterator.next();
            else throw new MissingTokenException(delimiterToken.toString()+ " : " + result);
        }
        result.add(current);
        return result;
    }

    ///-------
    //delimiterToken
        // if nestedCounter = 0 -> return
        // else nestedCounter > 0 -> nestedCounter-- && continue
    //nestedToken
        // nestedCounter ++ && continue
    //otherToken
        // add Token
        // repeat
            // if hasNext
            // else through Exception (cut no End found)
    ///-------
    //delimiterToken
        // if nestedCounter = 0 -> return
        // else nestedCounter > 0 -> nestedCounter-- && ignore/act like otherToken

    //otherToken
        // if nestedToken -> nestedCounter++
        // also always ->
            // add Token
            // repeat
                // if hasNext
                // else through Exception (cut no End found)


    public void advance() {
        if (iterator.hasNext()){
            current = iterator.next();
        } else {
            throw new ArrayIndexOutOfBoundsException("Keine weiteren Tokens vorhanden.");
        }
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
//
//        tokens.add(new Token(null, 1, TokenType.BRACES_OPEN));
////        tokens.add(new Token("print", 1, TokenType.IDENTIFIER));          //PRIMITIVE FUNCTION.. not implemented yet
////        tokens.add(new Token(null, 1, TokenType.BRACKET_OPEN));
////        tokens.add(new Token("\"awman\"", 1, TokenType.STRING));
////        tokens.add(new Token(null, 1, TokenType.BRACKET_CLOSED));
//        tokens.add(new Token("x", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
//        tokens.add(new Token("234", 1, TokenType.INT));
//
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token(null, 1, TokenType.BRACES_CLOSED));
//
//
//        tokens.add(new Token(null, 1, TokenType.IF));
//        tokens.add(new Token(null, 1, TokenType.BRACKET_OPEN));
//        tokens.add(new Token("funny_var2", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.NEQ));
//        tokens.add(new Token("100100134", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.BRACKET_CLOSED));
//
//        tokens.add(new Token(null, 1, TokenType.BRACES_OPEN));
////        tokens.add(new Token("print", 1, TokenType.IDENTIFIER));          //PRIMITIVE FUNCTION.. not implemented yet
////        tokens.add(new Token(null, 1, TokenType.BRACKET_OPEN));
////        tokens.add(new Token("\"awman\"", 1, TokenType.STRING));
////        tokens.add(new Token(null, 1, TokenType.BRACKET_CLOSED));
//        tokens.add(new Token("x", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
//        tokens.add(new Token("234", 1, TokenType.INT));
//
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//        tokens.add(new Token(null, 1, TokenType.BRACES_CLOSED));
//
//
////        tokens.add(new Token("3", 1, TokenType.INT));
////        tokens.add(new Token(null, 1, TokenType.LEQ));
////        tokens.add(new Token("4", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));
//
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

        System.out.println(tokens);
        Parser p = new Parser(tokens);
        p.parseStatements();

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

//        private ArrayList<Token> getOneLine(ArrayList<Token> fileTokens, int currentLine){
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
