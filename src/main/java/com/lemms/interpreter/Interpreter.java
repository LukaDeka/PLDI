package com.lemms.interpreter;

//für Exceptions
import com.lemms.Exceptions.LemmsRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lemms.SyntaxNode.*;
import com.lemms.api.NativeFunction;
import com.lemms.interpreter.FlowSignal.SignalType;

import com.lemms.TokenType;
import static com.lemms.TokenType.*;

public class Interpreter implements StatementVisitor, ValueVisitor {
    public Environment globalEnvironment;
    public Environment environment;
    public List<StatementNode> program;
    private final Map<String, NativeFunction> nativeFunctions;

    public Interpreter(List<StatementNode> program) {
        this.program = program;
        nativeFunctions = new HashMap<>();
        addPredefinedFunctions();
    }

    public Interpreter(List<StatementNode> program, Map<String, NativeFunction> nativeFunctions) {
        this.program = program;
        this.nativeFunctions = nativeFunctions;
        addPredefinedFunctions();
    }

    private void addPredefinedFunctions() {
        var predefinedFunctions = PredefinedFunctionLibrary.getPredefinedFunctions();
        for (Map.Entry<String, NativeFunction> entry : predefinedFunctions.entrySet()) {
            nativeFunctions.put(entry.getKey(), entry.getValue());
        }
    }

    public void interpret() {
        globalEnvironment = new Environment();
        environment = globalEnvironment;
        for (StatementNode i : program) {
            i.accept(this);
        }

    }

    @Override
    public FlowSignal visitIfStatement(IfNode ifNode) {

        if (isTrue(ifNode.condition.accept(this))) {
            environment = new Environment(environment);
            FlowSignal result = ifNode.ifBody.accept(this);
            environment = environment.enclosing;
            if (result != FlowSignal.NORMAL)
                return result;
        } else if (ifNode.elseStatement != null) {
            environment = new Environment(environment);
            FlowSignal result = ifNode.elseStatement.accept(this);
            environment = environment.enclosing;
            if (result != FlowSignal.NORMAL)
                return result;
        }
        return FlowSignal.NORMAL;
    }

    @Override
    public FlowSignal visitWhileStatement(WhileNode whileNode) {

        while (isTrue(whileNode.condition.accept(this))) {
            environment = new Environment(environment);
            FlowSignal result = whileNode.whileBody.accept(this);
            environment = environment.enclosing;
            if (result != FlowSignal.NORMAL)
                return result;
        }
        return FlowSignal.NORMAL;

    }

    @Override
    public FlowSignal visitBlockStatement(BlockNode blockNode) {
        environment = new Environment(environment);
        for (StatementNode statement : blockNode.statements) {
            FlowSignal result = statement.accept(this);
            if (result != FlowSignal.NORMAL) {
                environment = environment.enclosing;
                return result;
            }
        }
        environment = environment.enclosing;
        return FlowSignal.NORMAL;
    }

    @Override
    public FlowSignal visitAssignmentNode(AssignmentNode assignmentNode) {
        Object value = assignmentNode.rightHandSide.accept(this);
        environment.assign(assignmentNode.leftHandSide.name, value);
        return FlowSignal.NORMAL;
    }

    @Override
    public Object visitVariableValue(VariableNode variableNode) {
        Object value = environment.get(variableNode.name);
        if (value == null) //undefined, optional dedicated isDefined if needed?
            throw new LemmsRuntimeException(variableNode.token, "Undefined variable '" + variableNode.name + "'.");
        else return value;
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
            TokenType.OR, TokenType.NOT);

    private static List<TokenType> comparisonOperators = List.of(TokenType.EQ,
            NEQ,
            TokenType.GEQ,
            TokenType.LEQ,
            TokenType.GT,
            TokenType.LT);

    @Override
    public Object visitOperatorValue(OperatorNode operatorNode) {
        if (numericOperators.contains(operatorNode.operator.getType())) {
            return evaluateNumericOperator(operatorNode);
        } else if (booleanOperators.contains(operatorNode.operator.getType())) {
            return evaluateBooleanOperator(operatorNode);
        } else if (comparisonOperators.contains(operatorNode.operator.getType())) {
            return evaluateComparisonOperators(operatorNode);
        } else {
            throw new RuntimeException("Unknown operator: " + operatorNode.operator);
        }
    }

    private boolean evaluateComparisonOperators(OperatorNode operatorNode) {
        Object leftValue = operatorNode.leftOperand.accept(this);
        Object rightValue = operatorNode.rightOperand.accept(this);

        switch (operatorNode.operator.getType()) {
            case EQ:
                return leftValue.equals(rightValue);
            case NEQ:
                return !leftValue.equals(rightValue);
            default:
                break;
        }

        int leftValueInt = Integer.parseInt(operatorNode.leftOperand.accept(this).toString());
        int rightValueInt = Integer.parseInt(operatorNode.rightOperand.accept(this).toString());

        switch (operatorNode.operator.getType()) {
            case GT:
                return leftValueInt > rightValueInt;
            case LT:
                return leftValueInt < rightValueInt;
            case GEQ:
                return leftValueInt >= rightValueInt;
            case LEQ:
                return leftValueInt <= rightValueInt;
            default:
                break;
        }

        throw new RuntimeException("Unknown comparison operator: " + operatorNode.operator);
    }

    private Object evaluateBooleanOperator(OperatorNode operatorNode) {
        boolean leftValue = isTrue(operatorNode.leftOperand.accept(this));
        boolean rightValue = isTrue(operatorNode.rightOperand.accept(this));
        switch (operatorNode.operator.getType()) {
            case AND:
                return leftValue && rightValue;
            case OR:
                return leftValue || rightValue;
            case NOT:
                return !rightValue;
            default:
                throw new RuntimeException("Unknown operator: " + operatorNode.operator);
        }
    }

    private Object evaluateNumericOperator(OperatorNode operatorNode) {
        int leftValue = Integer.parseInt(operatorNode.leftOperand.accept(this).toString());
        int rightValue = Integer.parseInt(operatorNode.rightOperand.accept(this).toString());
        switch (operatorNode.operator.getType()) {
            case PLUS:
                return leftValue + rightValue;
            case MINUS:
                return leftValue - rightValue;
            case MULTIPLICATION:
                return leftValue * rightValue;
            case DIVISION:
                if (rightValue == 0) {
                    //throw new RuntimeException("Division by zero");
                    throw new LemmsRuntimeException(operatorNode.operator, "Division by zero.");
                }
                return leftValue / rightValue;
            case MODULO:
                if (rightValue == 0) {
                    //throw new RuntimeException("Division by zero");
                    throw new LemmsRuntimeException(operatorNode.operator, "Division by zero.");
                }
                return leftValue % rightValue;
            default:
                //throw new RuntimeException("Unknown operator: " + operatorNode.operator);
                throw new LemmsRuntimeException(operatorNode.operator, "Unknown operator: " + operatorNode.operator);

        }
    }

    private boolean isTrue(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }

    @Override
    public Object visitFunctionCallValue(FunctionCallNode functionNode) {

        if (nativeFunctions.containsKey((functionNode.functionName))) {
            NativeFunction nativeFunction = nativeFunctions.get(functionNode.functionName);
            List<Object> args = functionNode.params.stream()
                    .map(param -> param.accept(this))
                    .toList();
            return nativeFunction.apply(args);
        }

        Object functionValue = environment.get(functionNode.functionName);
        if (functionValue instanceof FunctionDeclarationNode) {
            List<Object> args = functionNode.params.stream()
                    .map(param -> param.accept(this))
                    .toList();

            Environment functionEnvironment = new Environment(globalEnvironment);
            for (int i = 0; i < args.size(); i++) {
                String argName = ((FunctionDeclarationNode) functionValue).paramNames.get(i);
                Object argValue = args.get(i);
                functionEnvironment.assign(argName, argValue);

            }
            Environment previousEnvironment = environment;
            environment = functionEnvironment;

            FlowSignal result = ((FunctionDeclarationNode) functionValue).functionBody.accept(this);
            if (result.signal == SignalType.RETURN || result.signal == SignalType.NORMAL) {
                environment = previousEnvironment; // Restore the previous environment
                return result.value;
            } else {
                throw new RuntimeException("Function did not return a value: " + functionNode.functionName);
            }
        }

        throw new RuntimeException("Unknown function: " + functionNode.functionName);
    }

    @Override
    public FlowSignal visitFunctionCallStatement(FunctionCallStatementNode functionNode) {
        functionNode.functionCall.accept(this);
        return FlowSignal.NORMAL;
    }

    @Override
    public void visitFunctionDeclarationStatement(FunctionDeclarationNode functionDeclarationNode) {

        environment.assign(functionDeclarationNode.functionName, functionDeclarationNode);
    }

    @Override
    public FlowSignal visitReturnNode(ReturnNode returnNode) {
        if(returnNode.value == null) {
            return FlowSignal.returned(null);
        }
        Object returnValue = returnNode.value.accept(this);
        return FlowSignal.returned(returnValue);
    }

    @Override
    public void visitClassDeclarationStatement(ClassDeclarationNode classDeclarationNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitClassDeclarationStatement'");
    }
}