package com.lemms.Canvas;

import java.awt.*;

public class Rect implements Drawable, Collidable {
    private int x, y, width, height;
    private Color color;

    public Rect(int x, int y, int w, int h, Color color) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public Color getColor() {return color;}

    @Override
    public int getX() {return x;}
    @Override
    public int getY() {return y;}
    @Override
    public int getWidth() {return width;}
    @Override
    public int getHeight() {return height;}

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int w) {this.width = w;}
    public void setHeight(int h) {this.height = h;}

    @Override
    public boolean intersects(Collidable other) {
        if (other == null) {
            return false;
        }
        return this.x < other.getX() + other.getWidth()
                && this.x + this.width > other.getX()
                && this.y < other.getY() + other.getHeight()
                && this.y + this.height > other.getY();
    }

    @Override
    public boolean contains(Collidable other) {
        if (other == null) {
            return false;
        }
        return other.getX() >= this.x
                && other.getY() >= this.y
                && other.getX() + other.getWidth() <= this.x + this.width
                && other.getY() + other.getWidth() <= this.y + this.height;
    }
}
