package com.lemms.Canvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Canvas implements ActionListener {

    public ScriptCallback update;

    private final CanvasPanel panel;
    private final JFrame frame;
    private final Timer timer;

    public Canvas(int tickRate, int width, int height) {
        frame = new JFrame();
        panel = new CanvasPanel();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
        update = panel::repaint;
        timer = new Timer(tickRate, this);
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public Rect getBounds() {return new Rect(0,0, getWidth(), getHeight(), null);}

    public void run() {
        timer.start();
    }

    public void quit() {
        timer.stop();
    }

    public void add(Drawable drawable) {
        panel.addElement(drawable);
    }

    public void remove(Drawable drawable) {panel.removeElement(drawable);}

    public void clear() {panel.clearElements();}

    public void repaint() {panel.repaint();}

    public void onKeyPress(int key, ScriptCallback callback) {
        panel.addKeyEvent(key, callback);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ( e.getSource() == timer)
        {
            update.call();
        }
    }

}
