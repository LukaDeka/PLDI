package com.lemms.interpreter;

//für Exceptions
import com.lemms.Exceptions.LemmsRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lemms.SyntaxNode.*;
import com.lemms.api.NativeFunction;
import com.lemms.interpreter.FlowSignal.SignalType;
import com.lemms.interpreter.object.LemmsBool;
import com.lemms.interpreter.object.LemmsData;
import com.lemms.interpreter.object.LemmsFunction;
import com.lemms.interpreter.object.LemmsInt;
import com.lemms.interpreter.object.LemmsObject;
import com.lemms.interpreter.object.LemmsString;
import com.lemms.TokenType;
import static com.lemms.TokenType.*;

public class Interpreter implements StatementVisitor, ValueVisitor {
    public Environment globalEnvironment;
    public Environment environment;
    public List<StatementNode> program;
    private final Map<String, NativeFunction> nativeFunctions;

    private static List<TokenType> numericOperators = List.of(TokenType.PLUS,
            TokenType.MINUS,
            TokenType.MULTIPLICATION,
            TokenType.DIVISION,
            TokenType.MODULO);

    private static List<TokenType> numericComparisonOperators = List.of(
            TokenType.GEQ,
            TokenType.LEQ,
            TokenType.GT,
            TokenType.LT);

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
        for (var entry : nativeFunctions.entrySet()) {
            globalEnvironment.assign(entry.getKey(), new LemmsFunction(entry.getValue()));
        }

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
        LemmsData dataValue = assignmentNode.rightHandSide.accept(this);
        environment.assign(assignmentNode.leftHandSide.name, dataValue);
        return FlowSignal.NORMAL;
    }

    @Override
    public LemmsData visitVariableValue(VariableNode variableNode) {
        LemmsData value = environment.get(variableNode.name);
        if (value == null) // undefined, optional dedicated isDefined if needed?
            throw new LemmsRuntimeException("Undefined variable '" + variableNode.name + "'.");
        else
            return value;
    }

    @Override
    public LemmsData visitLiteralValue(LiteralNode literalNode) {
        if (literalNode.value instanceof Integer) {
            return new LemmsInt((Integer) literalNode.value);
        } else if (literalNode.value instanceof String) {
            return new LemmsString((String) literalNode.value);
        } else if (literalNode.value instanceof Boolean) {
            return new LemmsBool((Boolean) literalNode.value);
        }
        throw new LemmsRuntimeException("Unknown literal type: " + literalNode.value.getClass().getSimpleName());
    }

    @Override
    public LemmsData visitOperatorValue(OperatorNode operatorNode) {

        LemmsData leftValue = operatorNode.leftOperand.accept(this);
        LemmsData rightValue = operatorNode.rightOperand.accept(this);
        TokenType operatorType = operatorNode.operator.getType();

        if (operatorType == EQ || operatorType == NEQ) {
            boolean result = evaluateEqualityOperator(leftValue, rightValue, operatorType);
            return new LemmsBool(result);
        }

        if (leftValue instanceof LemmsBool && rightValue instanceof LemmsBool) {
            boolean result = evaluateBooleanOperator(((LemmsBool) leftValue).value,
                    ((LemmsBool) rightValue).value, operatorType);
            return new LemmsBool(result);
        } else if (leftValue instanceof LemmsInt && rightValue instanceof LemmsInt) {

            if (numericOperators.contains(operatorType)) {
                int result = evaluateNumericOperator(((LemmsInt) leftValue).value,
                        ((LemmsInt) rightValue).value, operatorType);
                return new LemmsInt(result);
            } else if (numericComparisonOperators.contains(operatorType)) {
                boolean result = evaluateNumericComparisonOperator(((LemmsInt) leftValue).value,
                        ((LemmsInt) rightValue).value, operatorType);
                return new LemmsBool(result);
            }

        } else {
            throw new RuntimeException("Unknown operator: " + operatorNode.operator);
        }

        return new LemmsBool(false);

    }

    private boolean evaluateEqualityOperator(LemmsData leftValue, LemmsData rightValue, TokenType operator) {

        boolean result = false;
        if (leftValue.getClass() != rightValue.getClass()) {
            result = false;
        }
        if (leftValue instanceof LemmsObject && rightValue instanceof LemmsObject) {
            result = leftValue.equals(rightValue);
        } else if (leftValue instanceof LemmsInt && rightValue instanceof LemmsInt) {
            int leftValueInt = ((LemmsInt) leftValue).value;
            int rightValueInt = ((LemmsInt) rightValue).value;
            result = leftValueInt == rightValueInt;
        } else if (leftValue instanceof LemmsString && rightValue instanceof LemmsString) {
            String leftString = ((LemmsString) leftValue).value;
            String rightString = ((LemmsString) rightValue).value;
            result = leftString.equals(rightString);

        } else if (leftValue instanceof LemmsBool && rightValue instanceof LemmsBool) {
            boolean leftBool = ((LemmsBool) leftValue).value;
            boolean rightBool = ((LemmsBool) rightValue).value;
            result = leftBool == rightBool;
        } else {
            throw new RuntimeException("Unknown equality check for: "
                    + leftValue.getClass().getSimpleName() + " and " + rightValue.getClass().getSimpleName());
        }
        if (operator == EQ) {
            return result;
        } else if (operator == NEQ) {
            return !result;
        } else {
            throw new RuntimeException("Unknown equality operator: " + operator);
        }

    }

    private boolean evaluateNumericComparisonOperator(int leftValueInt, int rightValueInt, TokenType operator) {

        switch (operator) {
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

        throw new RuntimeException("Unknown comparison operator: " + operator);
    }

    private boolean evaluateBooleanOperator(boolean leftValue, boolean rightValue, TokenType operator) {
        switch (operator) {
            case AND:
                return leftValue && rightValue;
            case OR:
                return leftValue || rightValue;
            case NOT:
                return !rightValue;
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }

    private int evaluateNumericOperator(int leftValue, int rightValue, TokenType operator) {

        switch (operator) {
            case PLUS:
                return leftValue + rightValue;
            case MINUS:
                return leftValue - rightValue;
            case MULTIPLICATION:
                return leftValue * rightValue;
            case DIVISION:
                if (rightValue == 0) {
                    // throw new RuntimeException("Division by zero");
                    throw new LemmsRuntimeException("Division by zero.");
                }
                return leftValue / rightValue;
            case MODULO:
                if (rightValue == 0) {
                    // throw new RuntimeException("Division by zero");
                    throw new LemmsRuntimeException("Division by zero.");
                }
                return leftValue % rightValue;
            default:
                // throw new RuntimeException("Unknown operator: " + operatorNode.operator);
                throw new LemmsRuntimeException("Unknown operator: " + operator);

        }
    }

    private boolean isTrue(LemmsData object) {
        if (object == null)
            return false;
        if (object instanceof LemmsBool)
            return ((LemmsBool) object).value;
        throw new LemmsRuntimeException("Condition must be a boolean, but was: " + object.getClass().getSimpleName());
    }

    @Override
    public LemmsData visitFunctionCallValue(FunctionCallNode functionNode) {

        LemmsData functionValue = environment.get(functionNode.functionName);
        if (!(functionValue instanceof LemmsFunction)) {
            throw new LemmsRuntimeException(
                    "Function '" + functionNode.functionName + "' is not defined or not a function.");
        }
        LemmsFunction lemmsFunction = (LemmsFunction) functionValue;
        if (lemmsFunction.isNative) {
            NativeFunction nativeFunction = lemmsFunction.nativeFunction;
            List<LemmsData> args = functionNode.params.stream()
                    .map(param -> param.accept(this))
                    .toList();
            return nativeFunction.apply(args);
        } else {
            FunctionDeclarationNode functionDeclaration = lemmsFunction.functionDeclaration;
            List<LemmsData> args = functionNode.params.stream()
                    .map(param -> param.accept(this))
                    .toList();

            Environment functionEnvironment = new Environment(globalEnvironment);
            for (int i = 0; i < args.size(); i++) {
                String argName = functionDeclaration.paramNames.get(i);
                LemmsData argValue = args.get(i);
                functionEnvironment.assign(argName, argValue);

            }
            Environment previousEnvironment = environment;
            environment = functionEnvironment;

            FlowSignal result = functionDeclaration.functionBody.accept(this);
            if (result.signal == SignalType.RETURN || result.signal == SignalType.NORMAL) {
                environment = previousEnvironment; // Restore the previous environment
                return result.value;
            } else {
                throw new RuntimeException("Function did not return a value: " + functionNode.functionName);
            }
        }
    }

    @Override
    public FlowSignal visitFunctionCallStatement(FunctionCallStatementNode functionNode) {
        functionNode.functionCall.accept(this);
        return FlowSignal.NORMAL;
    }

    @Override
    public void visitFunctionDeclarationStatement(FunctionDeclarationNode functionDeclarationNode) {

        environment.assign(functionDeclarationNode.functionName, new LemmsFunction(functionDeclarationNode));
    }

    @Override
    public FlowSignal visitReturnNode(ReturnNode returnNode) {
        if (returnNode.value == null) {
            return FlowSignal.returned(null);
        }
        LemmsData returnValue = returnNode.value.accept(this);
        return FlowSignal.returned(returnValue);
    }

    @Override
    public void visitClassDeclarationStatement(ClassDeclarationNode classDeclarationNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitClassDeclarationStatement'");
    }
}