package com.lemms.interpreter;

import java.util.ArrayList;
import java.util.List;

import com.lemms.SyntaxNode.*;
import com.lemms.TokenType;

public class Interpreter implements StatementVisitor, ValueVisitor {
    public Environment environment;
    public List<StatementNode> program;
    public Interpreter(List<StatementNode> program) {
        this.program = program;
    }
    public void interpret() {
        Environment globalEnvironment = new Environment();
        environment = globalEnvironment;
        for (StatementNode i : program) {i.accept(this);}

    }

    @Override
    public void visitIfStatement(IfNode ifNode) {

        if (isTrue(ifNode.condition.accept(this))) {
            environment = new Environment(environment);
            ifNode.statement.accept(this);
            environment = environment.enclosing;
        } else if (ifNode.elseStatement != null) {
            environment = new Environment(environment);
            ifNode.elseStatement.accept(this);
            environment = environment.enclosing;
        }
        

    }

    @Override
    public void visitWhileStatement(WhileNode whileNode) {
        
        while (isTrue(whileNode.condition.accept(this))) {
            environment = new Environment(environment);
            whileNode.statement.accept(this);
            environment = environment.enclosing;
        }
        
    }

    @Override
    public void visitBlockStatement(BlockNode blockNode) {
        environment = new Environment(environment);
        for (StatementNode statement : blockNode.statements) {
            statement.accept(this);
        }
        environment = environment.enclosing;
    }

    @Override
    public void visitPrintStatement(PrintNode printNode) {
        Object value = printNode.printValue.accept(this);
        if (value instanceof String) {
            System.out.println((String) value);
        } else if (value instanceof Integer) {
            System.out.println((Integer) value);
        } else if (value instanceof Boolean) {
            System.out.println((Boolean) value);
        } else {
            System.out.println(value);
        }
    }

    @Override
    public void visitAssignmentNode(AssignmentNode assignmentNode) {
        Object value = assignmentNode.rightHandSide.accept(this);
        environment.assign(assignmentNode.leftHandSide.name.toString(), value);
    }

    @Override
    public Object visitVariableValue(VariableNode variableNode) {
        return environment.get(variableNode.name.toString());
    }

    @Override
    public Object visitLiteralValue(LiteralNode literalNode) {

        return literalNode.value;
    }

    private static List<TokenType> numericOperators = List.of(TokenType.PLUS,
            TokenType.MINUS,
            TokenType.MULTIPLICATION,
            TokenType.DIVISION,
            TokenType.MODULO);

    private static List<TokenType> booleanOperators = List.of(TokenType.AND,
            TokenType.OR);

    private static List<TokenType> comparisonOperators = List.of(TokenType.EQ,
            TokenType.NEQ,
            TokenType.GEQ,
            TokenType.LEQ,
            TokenType.GT,
            TokenType.LT);

    @Override
    public Object visitOperatorValue(OperatorNode operatorNode) {
        if (numericOperators.contains(operatorNode.operator)) {
            return evaluateNumericOperator(operatorNode);
        } else if (booleanOperators.contains(operatorNode.operator)) {
            return evaluateBooleanOperator(operatorNode);
        } else if (comparisonOperators.contains(operatorNode.operator)) {
            return evaluateComparisonOperators(operatorNode);
        } else {
            throw new RuntimeException("Unknown operator: " + operatorNode.operator);
        }
    }

    private Object evaluateComparisonOperators(OperatorNode operatorNode) {
        Object leftValue = operatorNode.leftOperand.accept(this);
        Object rightValue = operatorNode.rightOperand.accept(this);

        switch (operatorNode.operator) {
            case EQ:
                return leftValue.equals(rightValue);
            case NEQ:
                return !leftValue.equals(rightValue);
            case GT:
                return ((Comparable<Object>) leftValue).compareTo(rightValue) > 0;
            case LT:
                return ((Comparable<Object>) leftValue).compareTo(rightValue) < 0;
            case GEQ:
                return ((Comparable<Object>) leftValue).compareTo(rightValue) >= 0;
            case LEQ:
                return ((Comparable<Object>) leftValue).compareTo(rightValue) <= 0;
            default:
                throw new RuntimeException("Unknown operator: " + operatorNode.operator);
        }
    }

    private Object evaluateBooleanOperator(OperatorNode operatorNode) {
        boolean leftValue = isTrue(operatorNode.leftOperand.accept(this));
        boolean rightValue = isTrue(operatorNode.rightOperand.accept(this));
        switch (operatorNode.operator) {
            case AND:
                return leftValue && rightValue;
            case OR:
                return leftValue || rightValue;
            default:
                throw new RuntimeException("Unknown operator: " + operatorNode.operator);
        }
    }

    private Object evaluateNumericOperator(OperatorNode operatorNode) {
        int leftValue = (int) operatorNode.leftOperand.accept(this);
        int rightValue = (int) operatorNode.rightOperand.accept(this);
        switch (operatorNode.operator) {
            case PLUS:
                return leftValue + rightValue;
            case MINUS:
                return leftValue - rightValue;
            case MULTIPLICATION:
                return leftValue * rightValue;
            case DIVISION:
                if (rightValue == 0) {
                    throw new RuntimeException("Division by zero");
                }
                return leftValue / rightValue;
            case MODULO:
                if (rightValue == 0) {
                    throw new RuntimeException("Division by zero");
                }
                return leftValue % rightValue;
            default:
                throw new RuntimeException("Unknown operator: " + operatorNode.operator);

        }
    }

    private boolean isTrue(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }
}