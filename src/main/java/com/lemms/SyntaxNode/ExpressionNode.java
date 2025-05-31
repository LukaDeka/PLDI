package com.lemms.SyntaxNode;

import com.lemms.Exceptions.MissingTokenException;
import com.lemms.Exceptions.UnexpectedToken;
import com.lemms.Token;
import com.lemms.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;

public abstract class ExpressionNode extends Node {

    private static final Logger log = LoggerFactory.getLogger(ExpressionNode.class);

    public static ExpressionNode parse(List<Token> tokens) {
        //toDO: Parsing Expression to other Nodes like BinaryOperator or single Value Node
        //      Note the Precedence and Association!
        return null;
    }
}
