package com.lemms.Canvas;

import com.lemms.interpreter.StatementVisitor;

public interface ScriptCallback {
    void call(StatementVisitor statementVisitor);
}
