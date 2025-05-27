package com.lemms;

import org.junit.jupiter.api.Test;

import com.lemms.Token.TokenType;
import com.lemms.SyntaxNode.*;
import com.lemms.interpreter.Interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

// filepath: src/test/java/com/lemms/MainTest.java
class InterpreterTest {

    @Test
    void testAssignment() {
        String variableName = "meaningOfLife";
        int value = 9;
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.leftHandSide = new VariableNode();
        assignmentNode.leftHandSide.name = variableName;        
        assignmentNode.rightHandSide = new LiteralNode(value);

        Interpreter interpreter = new Interpreter(assignmentNode);
        interpreter.interpret();        

        assertEquals(interpreter.environment.get(variableName), value);
    }

    @Test
    void testAssignmentWithOperator() {
        String variableName = "sum";
        int value1 = 9;
        int value2 = 18;
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.leftHandSide = new VariableNode();
        assignmentNode.leftHandSide.name = variableName;        
        OperatorNode operatorNode = new OperatorNode();
        operatorNode.operator = TokenType.ADDITION;
        operatorNode.leftOperand = new LiteralNode(value1);
        operatorNode.rightOperand = new LiteralNode(value2);
        assignmentNode.rightHandSide = operatorNode;

        Interpreter interpreter = new Interpreter(assignmentNode);
        interpreter.interpret();        

        assertEquals(interpreter.environment.get(variableName), value1 + value2);
    }

    @Test
    void testDoubleAssignment() {
        String variableName1 = "var1";
        String variableName2 = "var2";
        int value = 27;
        
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.leftHandSide = new VariableNode();
        assignmentNode.leftHandSide.name = variableName1;        
        assignmentNode.rightHandSide = new LiteralNode(value);

        AssignmentNode assignmentNode2 = new AssignmentNode();
        assignmentNode2.leftHandSide = new VariableNode();
        assignmentNode2.leftHandSide.name = variableName2;        
        assignmentNode2.rightHandSide = assignmentNode.leftHandSide;

        BlockNode blockNode = new BlockNode();
        blockNode.statements = List.of(assignmentNode, assignmentNode2);

        Interpreter interpreter = new Interpreter(blockNode);
        interpreter.interpret();        

        assertEquals(interpreter.environment.get(variableName1), interpreter.environment.get(variableName2));
    }
}