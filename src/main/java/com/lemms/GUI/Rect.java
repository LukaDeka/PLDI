package com.lemms.GUI;

import java.awt.*;

public class Rect extends RigidBody implements Drawable {
    Color color;

    public Rect(int x, int y, int w, int h, Color color) {
        super(x, y, w, h);
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    public int setX(int x) {
        this.x = x;
        return x;
    }
}
