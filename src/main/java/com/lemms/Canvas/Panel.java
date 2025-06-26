package com.lemms.Canvas;

import com.lemms.interpreter.StatementVisitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Panel extends JPanel implements KeyListener {
    private final HashMap<String, CanvasObject> elements = new HashMap<>();
    private final HashMap<Integer, ScriptCallback> keyEvents = new HashMap<>();
    private final StatementVisitor statementVisitor;

    public Panel(StatementVisitor statementVisitor) {
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        this.statementVisitor = statementVisitor;
    }

    public void addElement(String id, CanvasObject o) {
        elements.put(id, o);
    }

    public void moveElement(String id, int x, int y) {
        elements.get(id).setX(x);
        elements.get(id).setY(y);
    }

    public void moveElement(String id, int x, int y, int w, int h) {
        elements.get(id).setX(x);
        elements.get(id).setY(y);
        elements.get(id).setWidth(w);
        elements.get(id).setHeight(h);
    }

    public void removeElement(String id) {
        elements.remove(id);
    }

    public void clearElements() {
        elements.clear();
    }

    public void addKeyEvent(int key, ScriptCallback callback) {
        keyEvents.put(key, callback);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Drawable d : elements.values()) {
            d.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(keyEvents.containsKey(e.getKeyCode())) {
            keyEvents.get(e.getKeyCode()).call(statementVisitor);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
