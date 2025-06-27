package com.lemms.Canvas;

import java.awt.*;

public class Rect extends CanvasObject implements Drawable {
    private Color color;

    public Rect(int x, int y, int w, int h, Color color) {
        super(x, y, w, h);
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}
}
