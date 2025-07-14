package com.lemms.interpreter;

//für Exceptions
import com.lemms.Exceptions.LemmsParseError;
import com.lemms.Exceptions.LemmsRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lemms.SyntaxNode.*;
import com.lemms.Token;
import com.lemms.Tokenizer;
import com.lemms.api.NativeFunction;
import com.lemms.interpreter.FlowSignal.SignalType;
import com.lemms.interpreter.object.LemmsBool;
import com.lemms.interpreter.object.LemmsData;
import com.lemms.interpreter.object.LemmsFunction;
import com.lemms.interpreter.object.LemmsInt;
import com.lemms.interpreter.object.LemmsObject;
import com.lemms.interpreter.object.LemmsString;
import com.lemms.TokenType;
import com.lemms.parser.ExpressionParser;
import com.lemms.parser.Parser;

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

    private boolean useClassEnvironmentSignal = false;

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

    public void initializeForRepl() {
        globalEnvironment = new Environment();
        environment = globalEnvironment;
        for (var entry : nativeFunctions.entrySet()) {
            globalEnvironment.assign(entry.getKey(), new LemmsFunction(entry.getValue()));
        }
    }

    //FÜR REPL
    public void interpretLine(String source) {
        Tokenizer tokenizer = new Tokenizer(source);
        List<Token> tokens = tokenizer.getTokens();

        if (tokens.isEmpty()) {
            return;
        }

        try {
            // ATTEMPT 1: als Statement(s) parsen
            Parser parser = new Parser(tokens);
            List<StatementNode> statements = parser.parse();

            // alle statements ausführen
            for (StatementNode stmt : statements) {
                stmt.accept(this);
            }

            // eine *kontextabhängige* Bestätigung (nach erfolgreiche Ausführrung).
            if (!statements.isEmpty()) {
                StatementNode lastStatement = statements.get(statements.size() - 1);

                // Hat die letzte Anweisung die Ausgabezeile "offen" gelassen?
                if (isPrintWithoutNewlineCall(lastStatement)) {
                    System.out.println(); // Ja, also schließe die Zeile mit einem Zeilenumbruch.
                }

                // Jetzt die normale REPL-Bestätigung ausgeben
                if (lastStatement instanceof AssignmentNode assignment) {
                    ExpressionNode assignedExpr = assignment.leftHandSide;
                    if (assignedExpr instanceof VariableNode varNode) {
                        System.out.println("=> " + environment.get(varNode.name));
                    } else {
                        System.out.println("=> null");
                    }
                } else if (lastStatement instanceof FunctionDeclarationNode funcDecl) {
                    System.out.println("=> [Function: " + funcDecl.functionName + "]");
                } else {
                    // alle rrstlichen Statements (if, while, println, etc.)
                    System.out.println("=> null");
                }
            }

        } catch (LemmsParseError statementParseError) {
            // ATTEMPT 2: als Expression parsen
            try {
                ExpressionParser exprParser = new ExpressionParser(tokens);
                ExpressionNode expression = exprParser.parseExpression();

                LemmsData result = expression.accept(this);

                if (result != null) {
                    System.out.println("=> " + result);
                } else {
                    System.out.println("=> null");
                }

            } catch (Exception expressionParseError) {
                throw statementParseError;
            }
        }
    }

    //helperfunction (for clean REPL output)
    private boolean isPrintWithoutNewlineCall(StatementNode stmt) {
        if (stmt instanceof FunctionCallStatementNode fcs) {
            if (fcs.functionCall != null) {
                return fcs.functionCall.functionName.equals("print");
            }
        }
        return false;
    }

    private void addPredefinedFunctions() {
        var predefinedFunctions = PredefinedFunctionLibrary.getPredefinedFunctions();
        for (Map.Entry<String, NativeFunction> entry : predefinedFunctions.entrySet()) {
            nativeFunctions.put(entry.getKey(), entry.getValue());
        }
    }
    
    public void addNativeFunctions(Map<String, NativeFunction> nativeFunctions) {
        this.nativeFunctions.putAll(nativeFunctions);
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
        LemmsData rightValue = assignmentNode.rightHandSide.accept(this);

        if (assignmentNode.leftHandSide instanceof VariableNode variableNode) {
            // Simple variable assignment: x = 5
            environment.assign(variableNode.name, rightValue);
        } else if (assignmentNode.leftHandSide instanceof MemberAccessNode memberAccessNode) {
            // Member access assignment: h.age = 25 or h.name.first = "John"
            assignToMemberAccess(memberAccessNode, rightValue);
        } else {
            throw new LemmsRuntimeException(
                    "Invalid assignment target: " + assignmentNode.leftHandSide.getClass().getSimpleName());
        }
        return FlowSignal.NORMAL;
    }

    private void assignToMemberAccess(MemberAccessNode memberAccess, LemmsData value) {
        if (memberAccess.child == null) {
            // This is the final property to assign to
            if (memberAccess.object instanceof VariableNode varNode) {
                environment.assign(varNode.name, value);
            } else {
                throw new LemmsRuntimeException("Complex member access assignment not yet supported.");
            }
        } else {
            // Navigate through the member access chain
            LemmsData current = memberAccess.object.accept(this);
            if (!(current instanceof LemmsObject lemmsObj)) {
                throw new LemmsRuntimeException("Cannot access member of non-object.");
            }

            // Switch to the object's environment for the rest of the assignment
            Environment previousEnv = environment;
            environment = lemmsObj.environment;
            try {
                assignToMemberAccess(memberAccess.child, value);
            } finally {
                environment = previousEnv;
            }
        }
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

        } else if (leftValue instanceof LemmsString || rightValue instanceof LemmsString) {
            if (operatorType == PLUS) {
                String leftString = leftValue instanceof LemmsString ? ((LemmsString) leftValue).value
                        : leftValue.toString();
                String rightString = rightValue instanceof LemmsString ? ((LemmsString) rightValue).value
                        : rightValue.toString();
                return new LemmsString(leftString + rightString);
            } else {
                throw new LemmsRuntimeException("Operator '" + operatorType + "' not supported for strings.");
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

            Environment functionEnvironment = new Environment(
                    useClassEnvironmentSignal ? environment : globalEnvironment);
            useClassEnvironmentSignal = false;

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
        if (functionNode.functionCall != null) {
            // Simple function call
            functionNode.functionCall.accept(this);
        } else if (functionNode.expression != null) {
            // Member access function call - this preserves the object context
            functionNode.expression.accept(this);
        }
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

        NativeFunction constructor = (args) -> {
            LemmsObject lemmsObject = new LemmsObject(classDeclarationNode, globalEnvironment);

            for (int i = 0; i < classDeclarationNode.localVariables.size(); i++) {
                String paramName = classDeclarationNode.localVariables.get(i);
                LemmsData paramValue = args.get(i);
                lemmsObject.set(paramName, paramValue);
            }
            for (var functionDeclaration : classDeclarationNode.localFunctions) {
                lemmsObject.set(functionDeclaration.functionName, new LemmsFunction(functionDeclaration));
            }

            return lemmsObject;
        };

        globalEnvironment.assign(classDeclarationNode.className,
                new LemmsFunction(constructor));
    }

    @Override
    public LemmsData visitMemberAccessValue(MemberAccessNode node) {
        // Evaluate the current member (could be a variable or function call)
        LemmsData current = node.object.accept(this);

        // If there is no further child, return the resolved value
        if (node.child == null) {
            return current;
        }

        // If the current value is not an object, we cannot access further members
        if (!(current instanceof LemmsObject lo)) {
            throw new LemmsRuntimeException("Cannot access member of non-object.");
        }

        // Set the environment to the object's environment for the next access in the
        // chain
        Environment previousEnvironment = environment;
        environment = lo.environment;
        if (node.child.object instanceof FunctionCallNode) {
            useClassEnvironmentSignal = true;
        }
        LemmsData result = visitMemberAccessValue(node.child);
        environment = previousEnvironment;
        return result;
    }

}