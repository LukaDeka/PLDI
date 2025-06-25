package com.lemms.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Snake {
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

        canvas.run();
        canvas.add(snake.get(0));
        canvas.add(food);

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

        canvas.update = () -> {
            // Compute next head position
            Rect oldHead = snake.get(0);
            int newX = oldHead.getX() + dir[0]*20;
            int newY = oldHead.getY() + dir[1]*20;
            Rect newHead = new Rect(newX, newY, 20, 20, Color.BLUE);

            // Check wall collision
            if (!canvas.getBounds().contains(newHead)) {
                GameOver(canvas, snake, false);
                return;
            }

            // Self‐collision
            for (Rect segment : snake) {
                if (segment.getX() == newX && segment.getY() == newY) {
                    // game over
                    GameOver(canvas, snake, false);
                    return;
                }
            }

            // Add head to front
            snake.add(0, newHead);
            canvas.add(newHead);

            boolean ate = newHead.intersects(food);
            if (ate) {
                // move the food
                food.setX(random.nextInt(gridWidth-1)*20);
                food.setY(random.nextInt(gridHeight-1)*20);

                if(snake.size() == gridHeight*gridWidth) {
                    GameOver(canvas, snake, true);
                }
            }

            if (!ate) {
                Rect tail = snake.remove(snake.size() - 1);
                canvas.remove(tail);
            }

            canvas.repaint();
        };
    }

    private static void GameOver(Canvas canvas, ArrayList<Rect> snake, boolean won) {
        canvas.clear();
        snake.clear();
        String text = won ? "Game Won!" : "Game Over!";
        Text GameOver = new Text(text,canvas.getWidth()/2, canvas.getHeight()/2, 72,
                Color.BLACK);
        GameOver.align(0,0);
        canvas.add(GameOver);
        canvas.repaint();
        canvas.quit();
    }

}
