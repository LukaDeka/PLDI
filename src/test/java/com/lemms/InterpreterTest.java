package com.lemms;

import org.junit.jupiter.api.Test;
import com.lemms.SyntaxNode.*;
import com.lemms.interpreter.Interpreter;

import static org.junit.jupiter.api.Assertions.fail;

// filepath: src/test/java/com/lemms/MainTest.java
class interpreterTests {

    @Test
    void testMain() {
        Interpreter interpreter = new Interpreter();
        WhileNode whileNode = new WhileNode();
        whileNode.statement = new BlockNode();
        
        interpreter.interpret(whileNode);
        fail();
    }
}