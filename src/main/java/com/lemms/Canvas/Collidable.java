package com.lemms.Canvas;

public interface Collidable {
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    boolean intersects(Collidable other);
    boolean contains(Collidable other);
}
