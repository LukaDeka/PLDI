package com.lemms.Canvas;

import java.awt.*;

public class Pixel extends CanvasObject implements Drawable {
    private int x, y;
    private Color color;

    public Pixel(int x, int y, Color color) {
        super(x, y, 1, 1);
        this.color = color;
    }

    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, 1, 1); // Or larger if needed
    }
}