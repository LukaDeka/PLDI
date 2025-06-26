package com.lemms.Canvas;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Text extends CanvasObject implements Drawable {
    private String text;
    private int size;
    private Color color;

    // Alignment factors: -1 = top/left, 0 = center, +1 = bottom/right
    private float alignX = -1f;
    private float alignY = -1f;

    public Text(String text, int x, int y, int size, Color color) {
        super(x, y, 0,0);
        this.text  = text;
        this.size  = size;
        this.color = color;
    }

    public void align(float alignX, float alignY) {
        this.alignX = alignX;
        this.alignY = alignY;
    }

    // Getter and Setter
    public String getText()   { return text; }
    public int    getSize()   { return size; }
    public Color  getColor()  { return color; }

    public void setText(String text)   { this.text = text; }
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

        if (getWidth() == 0 && getHeight() == 0) {
            setWidth(textWidth);
            setHeight(textHeight);
        }

        // Compute offsets: (-1 -> 0, 0 -> 0.5, +1 -> 1)
        float fx = (alignX + 1f) / 2f;
        float fy = (alignY + 1f) / 2f;
        int offsetX = Math.round(fx * textWidth);
        int offsetY = Math.round(fy * textHeight);

        // Draw at reference point minus offset
        g.drawString(text, getX() - offsetX, getY() - offsetY);

        // Restore graphics state
        g.setFont(oldFont);
        g.setColor(oldColor);
    }
}