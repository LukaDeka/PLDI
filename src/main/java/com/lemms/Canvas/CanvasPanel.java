package com.lemms.Canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CanvasPanel extends JPanel implements KeyListener {
    private final List<Drawable> elements = new ArrayList<>();
    private final HashMap<Integer, ScriptCallback> keyEvents = new HashMap<>();

    public CanvasPanel() {
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    public void addElement(Drawable d) {
        elements.add(d);
    }

    public void removeElement(Drawable d) {
        elements.remove(d);
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
        for (Drawable d : elements) {
            d.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(keyEvents.containsKey(e.getKeyCode())) {
            keyEvents.get(e.getKeyCode()).call();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
