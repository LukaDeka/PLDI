package com.lemms.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Text implements Drawable {
    private String text;
    private int x;
    private int y;
    private int size;
    private Color color;

    // Alignment factors: -1 = top/left, 0 = center, +1 = bottom/right
    private float alignX = -1f;
    private float alignY = -1f;

    public Text(String text, int x, int y, int size, Color color) {
        this.text  = text;
        this.x     = x;
        this.y     = y;
        this.size  = size;
        this.color = color;
    }

    public void align(float alignX, float alignY) {
        this.alignX = alignX;
        this.alignY = alignY;
    }

    public String getText()   { return text; }
    public int    getX()      { return x; }
    public int    getY()      { return y; }
    public int    getSize()   { return size; }
    public Color  getColor()  { return color; }

    public void setText(String text)   { this.text = text; }
    public void setX(int x)            { this.x = x; }
    public void setY(int y)            { this.y = y; }
    public void setSize(int size)      { this.size = size; }
    public void setColor(Color color)  { this.color = color; }

    public void draw(Graphics g) {
        Font oldFont = g.getFont();
        Color oldColor = g.getColor();

        // Apply font size
        Font newFont = oldFont.deriveFont((float) size);
        g.setFont(newFont);
        g.setColor(color);

        // Measure text
        FontMetrics fm = g.getFontMetrics(newFont);
        int textWidth  = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // Compute offsets: (-1 -> 0, 0 -> 0.5, +1 -> 1)
        float fx = (alignX + 1f) / 2f;
        float fy = (alignY + 1f) / 2f;
        int offsetX = Math.round(fx * textWidth);
        int offsetY = Math.round(fy * textHeight);

        // Draw at reference point minus offset
        g.drawString(text, x - offsetX, y - offsetY);

        // Restore graphics state
        g.setFont(oldFont);
        g.setColor(oldColor);
    }
}