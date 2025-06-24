package com.lemms.GUI;

public class RigidBody {
    public int x;
    public int y;
    public int width;
    public int height;

    public RigidBody(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public boolean intersects(RigidBody other) {
        if (other == null) {
            return false;
        }
        return this.x < other.x + other.width
                && this.x + this.width > other.x
                && this.y < other.y + other.height
                && this.y + this.height > other.y;
    }

    public boolean contains(RigidBody other) {
        if (other == null) {
            return false;
        }
        return other.x >= this.x
                && other.y >= this.y
                && other.x + other.width <= this.x + this.width
                && other.y + other.height <= this.y + this.height;
    }
}
