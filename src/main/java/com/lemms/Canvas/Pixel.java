package com.lemms.Canvas;

import java.awt.*;

public class Pixel implements Drawable {
    private int x, y;
    private Color color;

    public Pixel(int x, int y, Color color) {
        this.x = x; this.y = y; this.color = color;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public Color getColor() {return color;}

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setColor(Color color) {this.color = color;}

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, 1, 1); // Or larger if needed
    }
}