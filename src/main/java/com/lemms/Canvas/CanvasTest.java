package com.lemms.Canvas;


import java.awt.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Frame;

public class CanvasTest {
    public static void main(String[] args) {
        Frame frame = new Frame("Mein Canvas Fenster");
        frame.setSize(400, 300);   //Fenster

        MeinCanvas canvas = new MeinCanvas();
        canvas.setSize(400, 300);
        canvas.setBackground(Color.WHITE);

        frame.add(canvas);
        frame.setVisible(true);
    }

    static class MeinCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(50, 50, 100, 100); // Ein blaues Quadrat zeichnen
        }
    }
}


