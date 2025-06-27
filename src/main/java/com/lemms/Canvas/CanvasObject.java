package com.lemms.Canvas;

public abstract class CanvasObject implements Drawable {
    private int x, y, width, height;

    public CanvasObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}

    public boolean intersects(CanvasObject other){
        if (other == null) {
            return false;
        }
        return this.x < other.getX() + other.getWidth()
                && this.x + this.width > other.getX()
                && this.y < other.getY() + other.getHeight()
                && this.y + this.height > other.getY();
    }
    public boolean contains(CanvasObject other){
        if (other == null) {
            return false;
        }
        return other.getX() >= this.x
                && other.getY() >= this.y
                && other.getX() + other.getWidth() <= this.x + this.width
                && other.getY() + other.getWidth() <= this.y + this.height;
    }

}
