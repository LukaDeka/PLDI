package com.lemms;

import org.junit.jupiter.api.Test;
import com.lemms.SyntaxNode.*;
import com.lemms.interpreter.Interpreter;
import com.lemms.interpreter.object.LemmsData;
import com.lemms.interpreter.object.LemmsInt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

// filepath: src/test/java/com/lemms/MainTest.java
class InterpreterTest {

    @Test
    void testAssignment() {
        String variableName = "meaningOfLife";
        int value = 9;
        // meaningOfLife = 0
        VariableNode variableNode = new VariableNode(variableName);
        ExpressionNode expressionNode = new LiteralNode(value);
        AssignmentNode assignmentNode = new AssignmentNode(variableNode, expressionNode);

        Interpreter interpreter = new Interpreter(List.of(assignmentNode));
        interpreter.interpret();

        LemmsData result = interpreter.environment.get(variableName);
        assertInstanceOf(LemmsInt.class, result);
        assertEquals(value, ((LemmsInt) result).value);
    }

    @Test
    void testAssignmentWithOperator() {
        String variableName = "sum";
        int value1 = 9;
        int value2 = 18;

        Token operator = new Token(TokenType.PLUS);
        ExpressionNode leftOperand = new LiteralNode(value1);
        ExpressionNode rightOperand = new LiteralNode(value2);

        ExpressionNode expressionNode = new OperatorNode(leftOperand, operator, rightOperand);
        VariableNode variableNode = new VariableNode(variableName);
        AssignmentNode assignmentNode = new AssignmentNode(variableNode, expressionNode);

        Interpreter interpreter = new Interpreter(List.of(assignmentNode));
        interpreter.interpret();

        LemmsData result = interpreter.environment.get(variableName);
        assertInstanceOf(LemmsInt.class, result);        
        assertEquals(value1 + value2, ((LemmsInt)result).value);
    }

    @Test
    void testDoubleAssignment() {
        String variableName1 = "var1";
        String variableName2 = "var2";
        int value = 27;

        VariableNode variableNode = new VariableNode(variableName1);
        ExpressionNode expressionNode = new LiteralNode(value);
        AssignmentNode assignmentNode = new AssignmentNode(variableNode, expressionNode);

        VariableNode variableNode2 = new VariableNode(variableName2);
        ExpressionNode expressionNode2 = new LiteralNode(value);
        AssignmentNode assignmentNode2 = new AssignmentNode(variableNode2, expressionNode2);

        List<StatementNode> program = List.of(assignmentNode, assignmentNode2);

        Interpreter interpreter = new Interpreter(program);
        interpreter.interpret();

        assertEquals(((LemmsInt)interpreter.environment.get(variableName1)).value, 
        ((LemmsInt)interpreter.environment.get(variableName2)).value);
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

        // n = 20
        VariableNode n1 = new VariableNode(variableNameN);
        ExpressionNode number20 = new LiteralNode(n);
        AssignmentNode assignmentNodeN = new AssignmentNode(n1, number20);

        // a = 0
        VariableNode a0 = new VariableNode(variableName1);
        ExpressionNode nummber0 = new LiteralNode(value1);
        AssignmentNode assignmentNode1 = new AssignmentNode(a0, nummber0);

        // b = 1
        VariableNode b0 = new VariableNode(variableName2);
        ExpressionNode number1 = new LiteralNode(value2);
        AssignmentNode assignmentNode2 = new AssignmentNode(b0, number1);

        // while n >= 1
        VariableNode n2 = new VariableNode(variableNameN);
        ExpressionNode number2 = new LiteralNode(1);
        OperatorNode condition = new OperatorNode(n2, new Token(TokenType.GEQ), number2);

        // temp = a + b
        VariableNode temp1 = new VariableNode("temp");

        Token plus = new Token(TokenType.PLUS);
        VariableNode a1 = new VariableNode(variableName1);
        VariableNode b1 = new VariableNode(variableName2);
        ExpressionNode expressionNode3 = new OperatorNode(a1, plus, b1);

        AssignmentNode assignmentNodeTemp = new AssignmentNode(temp1, expressionNode3);

        // a = b
        VariableNode a2 = new VariableNode(variableName1);
        VariableNode b2 = new VariableNode(variableName2);
        AssignmentNode assignmentNode3 = new AssignmentNode(a2, b2);

        // b = temp
        VariableNode b3 = new VariableNode(variableName2);
        VariableNode temp3 = new VariableNode("temp");
        AssignmentNode assignmentNode4 = new AssignmentNode(b3, temp3);

        // n = n - 1

        VariableNode n5 = new VariableNode(variableNameN);
        ExpressionNode expressionNode5 = new LiteralNode(1);
        OperatorNode decrementN = new OperatorNode(n5, new Token(TokenType.MINUS), expressionNode5);
        VariableNode n4 = new VariableNode(variableNameN);
        AssignmentNode assignmentNode5 = new AssignmentNode(n4, decrementN);

        BlockNode blockNode = new BlockNode(
                List.of(assignmentNodeTemp, assignmentNode3, assignmentNode4, assignmentNode5));
        WhileNode whileBlock = new WhileNode();
        whileBlock.condition = condition;
        whileBlock.whileBody = blockNode;

        Interpreter interpreter = new Interpreter(
                List.of(assignmentNodeN, assignmentNode1, assignmentNode2, whileBlock));

        interpreter.interpret();

        assertEquals(interpreter.environment.get(variableName1), FIBONACCI_NUMBER_20TH);
    }
}