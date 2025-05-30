package com.lemms;

import org.junit.jupiter.api.Test;

import com.lemms.TokenType;
import com.lemms.SyntaxNode.*;
import com.lemms.interpreter.Interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

// filepath: src/test/java/com/lemms/MainTest.java
class InterpreterTest {
    

    @Test
    void testAssignment() {
        String variableName = "meaningOfLife";
        int value = 9;
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.leftHandSide = new VariableNode(variableName);
    
        assignmentNode.rightHandSide = new LiteralNode(value);

        Interpreter interpreter = new Interpreter(List.of(assignmentNode));
        interpreter.interpret();        

        assertEquals(interpreter.environment.get(variableName), value);
    }

    @Test
    void testAssignmentWithOperator() {
        String variableName = "sum";
        int value1 = 9;
        int value2 = 18;
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.leftHandSide = new VariableNode(variableName);        
        OperatorNode operatorNode = new OperatorNode();
        operatorNode.operator = TokenType.PLUS;
        operatorNode.leftOperand = new LiteralNode(value1);
        operatorNode.rightOperand = new LiteralNode(value2);
        assignmentNode.rightHandSide = operatorNode;

        Interpreter interpreter = new Interpreter(List.of(assignmentNode));
        interpreter.interpret();        

        assertEquals(interpreter.environment.get(variableName), value1 + value2);
    }

    @Test
    void testDoubleAssignment() {
        String variableName1 = "var1";
        String variableName2 = "var2";
        int value = 27;
        
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.leftHandSide = new VariableNode(variableName1);
        assignmentNode.leftHandSide.name = variableName1;        
        assignmentNode.rightHandSide = new LiteralNode(value);

        AssignmentNode assignmentNode2 = new AssignmentNode();
        assignmentNode2.leftHandSide = new VariableNode(variableName2);
        assignmentNode2.leftHandSide.name = variableName2;        
        assignmentNode2.rightHandSide = assignmentNode.leftHandSide;
        
        List<StatementNode> program = List.of(assignmentNode, assignmentNode2);

        Interpreter interpreter = new Interpreter(program);
        interpreter.interpret();        

        assertEquals(interpreter.environment.get(variableName1), interpreter.environment.get(variableName2));
    }
    
    private static final int FIBONACCI_NUMBER_20TH = 6765;
    
    @Test
    void testFibonacci() {
        String variableNameN = "n";
        String variableName1 = "a";
        String variableName2 = "b";
        int n = 20;
        int value1 = 0;
        int value2 = 1;
        
        // n = 10
        AssignmentNode assignmentNodeN = new AssignmentNode();
        assignmentNodeN.leftHandSide = new VariableNode(variableNameN);         
        assignmentNodeN.rightHandSide = new LiteralNode(n);

        // a = 0
        AssignmentNode assignmentNode = new AssignmentNode();
        assignmentNode.leftHandSide = new VariableNode(variableName1);        
        assignmentNode.rightHandSide = new LiteralNode(value1);

        // b = 1
        AssignmentNode assignmentNode2 = new AssignmentNode();
        assignmentNode2.leftHandSide = new VariableNode(variableName2);        
        assignmentNode2.rightHandSide = new LiteralNode(value2);
        
        // while n >= 1
        WhileNode whileNode = new WhileNode();
        OperatorNode operatorNode = new OperatorNode();
        whileNode.condition = operatorNode;
        operatorNode.operator = TokenType.GEQ;
        operatorNode.leftOperand = new VariableNode(variableNameN);
        operatorNode.rightOperand = new LiteralNode(1);

        // temp = a + b
        AssignmentNode assignmentNodeTemp = new AssignmentNode();
        assignmentNodeTemp.leftHandSide = new VariableNode("temp");        
        OperatorNode operatorNode2 = new OperatorNode();
        assignmentNodeTemp.rightHandSide = operatorNode2;
        operatorNode2.operator = TokenType.PLUS;
        operatorNode2.leftOperand = new VariableNode(variableName1);
        operatorNode2.rightOperand = new VariableNode(variableName2);

        // a = b
        AssignmentNode assignmentNode3 = new AssignmentNode();
        assignmentNode3.leftHandSide = new VariableNode(variableName1);
        assignmentNode3.rightHandSide = new VariableNode(variableName2);
        
        // b = temp
        AssignmentNode assignmentNode4 = new AssignmentNode();
        assignmentNode4.leftHandSide = new VariableNode(variableName2);
        assignmentNode4.rightHandSide = new VariableNode("temp");

        // n = n - 1
        AssignmentNode assignmentNode5 = new AssignmentNode();
        assignmentNode5.leftHandSide = new VariableNode(variableNameN);
        OperatorNode decrementN = new OperatorNode();
        assignmentNode5.rightHandSide = decrementN;
        decrementN.operator = TokenType.MINUS;
        decrementN.leftOperand = new VariableNode(variableNameN);
        decrementN.rightOperand = new LiteralNode(1);

        BlockNode whileBlock = new BlockNode();
        whileBlock.statements = List.of(assignmentNodeTemp, assignmentNode3, assignmentNode4, assignmentNode5);
        whileNode.statement = whileBlock;

        Interpreter interpreter = new Interpreter(List.of(assignmentNodeN, assignmentNode, assignmentNode2, whileNode));

        interpreter.interpret();        

        assertEquals(interpreter.environment.get(variableName1), FIBONACCI_NUMBER_20TH);
    }
}