package com.lemms.SyntaxNode;

import java.util.ArrayList;
import com.lemms.Parser;
import com.lemms.Token;
import com.lemms.TokenType;

import static com.lemms.TokenType.*;

abstract public class ConditionedBlock extends StatementNode{
    ExpressionNode condition;
    StatementNode thenBlock;

    public ConditionedBlock(ArrayList<Token> tokens) {
        TokenType typeOfConditionedBlock = tokens.get(0).getType();

        checkTokenList(
                tokens,
                size -> size >= (1+3+3),
                typeOfConditionedBlock, BRACKET_OPEN, null, BRACKET_CLOSED, BRACES_OPEN, null, BRACES_CLOSED);

        logger.info(String.format("%s\n----- %s BLOCK -----",tokens,typeOfConditionedBlock));
        TokenType blockOpenerKeyword = tokens.remove(0).getType();
        Parser subTreeParser = new Parser(tokens);
        ArrayList<Token> conditionTokens = subTreeParser.addAllTokensUntil(BRACKET_CLOSED);
        subTreeParser.advance();
        ArrayList<Token> thenTokens = subTreeParser.addAllTokensUntil(BRACES_CLOSED);

        logger.info(conditionTokens + "\n----- CONDITION -----");
        ExpressionNode condition = ExpressionNode.parse(conditionTokens);
        logger.info(thenTokens + "\n----- THEN BLOCK -----");
        BlockNode thenBlock = new BlockNode(thenTokens);

        this.condition = condition;
        this.thenBlock = thenBlock;
    }
}
