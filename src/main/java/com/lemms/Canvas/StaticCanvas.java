package com.lemms.Canvas;

import com.lemms.api.LemmsAPI;
import com.lemms.interpreter.StatementVisitor;
import com.lemms.interpreter.object.LemmsFunction;
import com.lemms.interpreter.object.LemmsInt;
import com.lemms.interpreter.object.LemmsObject;
import com.lemms.interpreter.object.LemmsString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaticCanvas implements ActionListener {

    private static StaticCanvas instance;
    private final StatementVisitor statementVisitor;

    public static void init(int width, int height, int tickRate, StatementVisitor visitor) {
        if (instance != null) {
            throw new IllegalStateException("Canvas already initialized");
        }
        instance = new StaticCanvas(width, height, tickRate, visitor);
    }

    public static StaticCanvas get() {
        if (instance == null) {
            throw new IllegalStateException("Canvas not initialized. Call Canvas.init(...) first.");
        }
        return instance;
    }

    public static void run()       { get().runInstance(); }
    public static void quit()      { get().quitInstance(); }
    public static int getWidth()   { return get().frame.getWidth(); }
    public static int getHeight()  { return get().frame.getHeight(); }
    public static void add(String id, CanvasObject o)        { get().panel.addElement(id, o); }
    public static void move(String id, int x, int y) {get().panel.moveElement(id, x, y);}
    public static void move(String id, int x, int y, int w, int h) {get().panel.moveElement(id, x, y, w, h);}
    public static void remove(String id)     { get().panel.removeElement(id); }
    public static void clear()                { get().panel.clearElements(); }
    public static void repaint()              { get().panel.repaint(); }
    public static void onKeyPress(int key, ScriptCallback cb) {
        get().panel.addKeyEvent(key, cb);
    }
    public static void onTick(ScriptCallback cb) {
        get().update = cb;
    }
    public static void addPrimitives(LemmsAPI api, StatementVisitor visitor) {
        api.registerFunction("init_canvas", params -> {
            LemmsInt width = (LemmsInt) params.get(0);
            LemmsInt height = (LemmsInt) params.get(1);
            LemmsInt tickRate = (LemmsInt) params.get(2);
            init(width.value, height.value, tickRate.value, visitor);
            return null;
        });
        api.registerFunction("start_canvas", params -> {run(); return null;});
        api.registerFunction("quit_canvas", params -> {quit(); return null;});
        api.registerFunction("canvas_width", params -> {getWidth(); return null;});
        api.registerFunction("canvas_height", params -> {getHeight(); return null;});
        api.registerFunction("add_rectangle", params -> {
            LemmsString id = (LemmsString) params.get(0);
            LemmsInt x = (LemmsInt) params.get(1);
            LemmsInt y = (LemmsInt) params.get(2);
            LemmsInt width = (LemmsInt) params.get(3);
            LemmsInt height = (LemmsInt) params.get(4);
            LemmsString color = (LemmsString) params.get(5);
            Rect rect = new Rect(x.value, y.value, width.value, height.value, Color.getColor(color.value));
            add(id.value, rect); return null;});
        api.registerFunction("remove_canvas_element", params -> {
            LemmsString id = (LemmsString) params.get(0);
            remove(id.value);
            return null;});
        api.registerFunction("move_canvas_element", params -> {
            LemmsString id = (LemmsString) params.get(0);
            LemmsInt x = (LemmsInt) params.get(1);
            LemmsInt y = (LemmsInt) params.get(2);
            if(params.size() == 5) {
                LemmsInt width = (LemmsInt) params.get(3);
                LemmsInt height = (LemmsInt) params.get(4);
                move(id.value, x.value, y.value, width.value, height.value);
            }else{
                move(id.value, x.value, y.value);
            }
            return null;
        });
        api.registerFunction("clear_canvas", params -> {clear(); return null;});
        api.registerFunction("repaint_canvas", params -> {repaint(); return null;});
        api.registerFunction("on_key_press", params -> {
            LemmsInt key = (LemmsInt) params.get(0);
            LemmsFunction function = (LemmsFunction) params.get(1);
            ScriptCallback cv = function.functionDeclaration.functionBody::accept;
            onKeyPress(key.value, cv); return null;
        });
        api.registerFunction("on_tick", params -> {
            LemmsFunction function = (LemmsFunction) params.get(0);
            ScriptCallback cv = function.functionDeclaration.functionBody::accept;
            onTick(cv); return null;
        });
    }

    public ScriptCallback update;
    private final Panel panel;
    private final JFrame frame;
    private final Timer timer;

    private StaticCanvas(int width, int height, int tickRate, StatementVisitor visitor) {
        frame = new JFrame();
        panel = new Panel(visitor);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
        statementVisitor = visitor;

        // default tick → repaint
        update = (v) -> panel.repaint();
        timer = new Timer(tickRate, this);
    }

    // ─── private instance methods ─────────────────────────────────────────────────
    private void runInstance() {
        timer.start();
    }

    private void quitInstance() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            update.call(statementVisitor);
        }
    }
}