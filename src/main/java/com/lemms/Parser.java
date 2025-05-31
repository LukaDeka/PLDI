package com.lemms;
import com.lemms.Exceptions.MissingTokenException;
import com.lemms.Exceptions.UnexpectedToken;
import com.lemms.SyntaxNode.*;
import com.lemms.parser.ExpressionParser;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.*;

import static com.lemms.TokenType.*;
import static com.lemms.TokenType.BRACES_CLOSED;


public class Parser {
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
    private final Iterator<Token> iterator;

    public ArrayList<StatementNode> getAST() {
        return rootNodes;
    }

    public Parser(ArrayList<Token> tokens) {
        iterator = tokens.iterator(); //Iterator is like a HEAD or POINTER going through the Tokens, starting from Token the very first Token
    }

    private AssignmentNode parseAssignment(ArrayList<Token> tokens) {
        //checkTokenList(tokens,size-> size >= 3, TokenType.IDENTIFIER, TokenType.ASSIGNMENT);

        logger.severe(tokens + "\n----- ASSIGNMENT -----");
        Token identifier = tokens.get(0);
        ArrayList<Token> expressionTokens = new ArrayList<>(tokens.subList(2, tokens.size()));

        logger.severe(identifier + "\n----- VARIABLE -----");
        VariableNode variableNode = new VariableNode(identifier);

        logger.severe(expressionTokens + "\n----- EXPRESSION -----");
        ExpressionNode expressionNode = new ExpressionParser(expressionTokens).parseExpression();

        return new AssignmentNode(variableNode, expressionNode);
    }


    private BlockNode parseBlock(ArrayList<Token> tokens, String blockType){
        //checkTokenList(tokens,size -> size >= 3, BRACES_OPEN, null, BRACES_CLOSED);
        logger.severe(String.format("%s \n----- %s BLOCK -----",tokens, blockType));

        //removing braces
        tokens.remove(0);
        tokens.remove(tokens.size()-1);

        Parser subTreeParser = new Parser(tokens);
        subTreeParser.parseStatements();

        switch (blockType){
            case "" -> {return new BlockNode(subTreeParser.getAST());}
            case "ELSE" -> {return new ElseNode(subTreeParser.getAST());}

        }
        return new BlockNode(subTreeParser.getAST());
    }

    private BlockNode parseBlock(ArrayList<Token> tokens){
        return parseBlock(tokens,"");
    }

    private ElseNode parseElse(ArrayList<Token> tokens){

        ArrayList<Token> blockTokens = new ArrayList<>(tokens.subList(1,tokens.size()));
        String blockType = tokens.get(0).getType().toString();
        return (ElseNode) parseBlock(blockTokens, blockType);
    }

    private ConditionedBlock parseConditionedBlock(ArrayList<Token> tokens){
        TokenType typeOfConditionedBlock = tokens.get(0).getType();

//        checkTokenList(
//                tokens,
//                size -> size >= (1+3+3),
//                typeOfConditionedBlock, BRACKET_OPEN, null, BRACKET_CLOSED, BRACES_OPEN, null, BRACES_CLOSED);

        logger.severe(String.format("%s\n----- %s BLOCK -----",tokens,typeOfConditionedBlock));
        TokenType blockOpenerKeyword = tokens.remove(0).getType();
        Parser subTreeParser = new Parser(tokens);
        ArrayList<Token> conditionTokens = subTreeParser.addAllTokensUntil(BRACKET_CLOSED);
        subTreeParser.advance();
        ArrayList<Token> thenTokens = subTreeParser.addAllTokensUntil(BRACES_CLOSED);

        logger.severe(conditionTokens + "\n----- CONDITION -----");
        ExpressionNode condition = new ExpressionParser(conditionTokens).parseExpression();
        logger.severe(thenTokens + "\n----- THEN BLOCK -----");
        BlockNode thenBlock = parseBlock(thenTokens);

        switch (blockOpenerKeyword) {
            case WHILE -> {return new WhileNode(condition, thenBlock);}
            case IF -> {return new IfNode(condition, thenBlock);}
            case ELIF -> {return new ElifNode(condition, thenBlock);}
            default -> throw new UnexpectedToken("undefined block type???: " + blockOpenerKeyword);
        }
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
                        IfNode ifNode = (IfNode) parseConditionedBlock(ifTokens);
                        rootNodes.add(ifNode);
                    }

                    case ELIF -> {
                        ArrayList<Token> elifTokens = addAllTokensUntil(TokenType.BRACES_CLOSED); //what about nested blocks??
                        Node lastNode = rootNodes.get(rootNodes.size() -1 );
                        if (lastNode instanceof IfNode ifNode) {
                            logger.info(elifTokens + "\n----- CREATE ELIF BLOCK -----");
                            ElifNode elifNode = (ElifNode) parseConditionedBlock(elifTokens);
                            ifNode.addElif(elifNode);
                        } else {
                            throw new UnexpectedToken("ElifNode must have corresponding IfNode");
                        }
                    }

                    case ELSE -> {
                        ArrayList<Token> elseToken = addAllTokensUntil(TokenType.BRACES_CLOSED); //what about nested blocks??
                        Node lastNode = rootNodes.get(rootNodes.size() -1 );
                        if (lastNode instanceof IfNode ifNode) {
                            logger.info(elseToken + "\n----- CREATE ELSE BLOCK -----");
                            ElseNode elseNode = parseElse(elseToken);


                            ifNode.addElseNode(elseNode);
                        } else {
                            throw new UnexpectedToken("ElseNode must have corresponding IfNode");
                        }
                    }

                    case WHILE -> {
                        ArrayList<Token> whileTokens = addAllTokensUntil(TokenType.BRACES_CLOSED); //what about nested blocks??
                        logger.info(whileTokens + "\n----- CREATE WHILE BLOCK -----");
                        WhileNode whileNode = (WhileNode) parseConditionedBlock(whileTokens);
                        rootNodes.add(whileNode);
                    }

                    case BRACES_OPEN -> {
                        ArrayList<Token> blockTokens = addAllTokensUntil(TokenType.BRACES_CLOSED);
                        logger.info(blockTokens + "\n----- CREATE BLOCK -----");
                        rootNodes.add(parseBlock(blockTokens));
                    }

                    default -> {
                        ArrayList<Token> assignmentNodes = addAllTokensUntil(TokenType.SEMICOLON);

                        if (assignmentNodes.size() <= 1) { //ignore for empty statements like ";;;" | .empty() wenn ohne SEMICOLON Token
                            logger.info("----- IGNORE EMPTY STATEMENT -----");
                            continue;
                        }
                        logger.info(assignmentNodes + "\n----- CREATE ASSIGNMENT NODE -----");
                        rootNodes.add(parseAssignment(assignmentNodes));

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
//        tokens.add(new Token("funny_var1", 1, TokenType.IDENTIFIER));
//        tokens.add(new Token(null, 1, TokenType.ASSIGNMENT));
//        tokens.add(new Token("100100134", 1, TokenType.INT));
//        tokens.add(new Token(null, 1, TokenType.SEMICOLON));

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
