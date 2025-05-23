package com.lemms.SyntaxNode;

import com.lemms.Parser;
import com.lemms.Token;

import java.util.ArrayList;
import java.util.List;

import static com.lemms.TokenType.*;

public class BlockNode extends StatementNode {
    List<? extends StatementNode> statements;

    public BlockNode(ArrayList<Token> tokens) {
        this(tokens, "");

    }

    public BlockNode(ArrayList<Token> tokens, String blockTypeName) {
        super();
        checkTokenList(tokens,size -> size >= 3, BRACES_OPEN, null, BRACES_CLOSED);
        logger.info(String.format("%s \n----- %s BLOCK -----",tokens, blockTypeName));

        //removing braces
        tokens.remove(0);
        tokens.remove(tokens.size()-1);

        Parser subTreeParser = new Parser(tokens);
        subTreeParser.parseStatements();
        this.statements = subTreeParser.getAST();
    }
}
