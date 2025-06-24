package com.lemms.GUI;

import java.awt.*;

public class Pixel implements Drawable {
    int x, y;
    Color color;

    public Pixel(int x, int y, Color color) {
        this.x = x; this.y = y; this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, 1, 1); // Or larger if needed
    }
}