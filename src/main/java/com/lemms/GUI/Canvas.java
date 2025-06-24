package com.lemms.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Canvas implements ActionListener {

    public final CanvasPanel panel;
    public ScriptCallback onTick;

    private final JFrame frame;
    private final Timer timer;


    public Canvas(int tickRate, int width, int height) {
        frame = new JFrame();
        panel = new CanvasPanel();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
        onTick = panel::repaint;
        timer = new Timer(tickRate, this);
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void onKeyPress(int key, ScriptCallback callback) {
        panel.addKeyEvent(key, callback);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ( e.getSource() == timer)
        {
            onTick.call();
        }
    }

    public static void main(String[] args) {
        //create canvas with single snake cell
        Canvas canvas = new Canvas(500, 500,500);
        ArrayList<Rect> snake = new ArrayList<>(){};
        snake.add(new Rect(0,0, 20,20, Color.BLUE));

        //generate food at random location
        int gridWidth = canvas.getWidth()/20;
        int gridHeight = canvas.getHeight()/20;

        Random random = new Random();
        int food_x = random.nextInt(gridWidth-1)*20;
        int food_y = random.nextInt(gridHeight-1)*20;
        Rect food = new Rect(food_x, food_y, 20, 20, Color.RED);

        canvas.start();
        canvas.panel.addElement(snake.get(0));
        canvas.panel.addElement(food);

        int[] dir = {1, 0};

        canvas.onKeyPress(KeyEvent.VK_W, ()->{
            dir[0] = 0;
            dir[1] = -1;
        });
        canvas.onKeyPress(KeyEvent.VK_A, ()->{
            dir[0] = -1;
            dir[1] = 0;
        });
        canvas.onKeyPress(KeyEvent.VK_S, ()->{
            dir[0] = 0;
            dir[1] = 1;
        });
        canvas.onKeyPress(KeyEvent.VK_D, ()->{
            dir[0] = 1;
            dir[1] = 0;
        });

        canvas.onTick = () -> {
            // Compute next head position
            Rect oldHead = snake.get(0);
            int newX = oldHead.x + dir[0]*20;
            int newY = oldHead.y + dir[1]*20;
            Rect newHead = new Rect(newX, newY, 20, 20, Color.BLUE);

            // Check wall collision
            RigidBody bounds = new RigidBody(0, 0, gridWidth*20, gridHeight*20);
            if (!bounds.contains(newHead)) {
                GameOver(canvas, snake, false);
                return;
            }

            // Self‐collision
            for (Rect segment : snake) {
                if (segment.x == newX && segment.y == newY) {
                    // game over
                    GameOver(canvas, snake, false);
                    return;
                }
            }

            // Add head to front
            snake.add(0, newHead);
            canvas.panel.addElement(newHead);

            boolean ate = newHead.intersects(food);
            if (ate) {
                // move the food
                food.x = random.nextInt(gridWidth-1)*20;
                food.y = random.nextInt(gridHeight-1)*20;

                if(snake.size() == gridHeight*gridWidth) {
                    GameOver(canvas, snake, true);
                }
            }

            if (!ate) {
                Rect tail = snake.remove(snake.size() - 1);
                canvas.panel.removeElement(tail);
            }

            canvas.panel.repaint();
        };
    }

    private static void GameOver(Canvas canvas, ArrayList<Rect> snake, boolean won) {
        canvas.panel.clearElements();
        snake.clear();
        String text = won ? "Game Won!" : "Game Over!";
        Text GameOver = new Text(text,canvas.getWidth()/2, canvas.getHeight()/2, 72,
                Color.BLACK);
        GameOver.align(0,0);
        canvas.panel.addElement(GameOver);
        canvas.panel.repaint();
        canvas.stop();
    }
}
