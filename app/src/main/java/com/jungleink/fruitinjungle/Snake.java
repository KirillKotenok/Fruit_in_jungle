package com.jungleink.fruitinjungle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Snake implements Cloneable{
    private int snake_x;
    private int snake_y;
    private int max_x;
    private int max_y;
    private Bitmap snake_model;
    private int snake_speed = 10;
    private Random random;
    private GameView gameView;
    private boolean is_drawable;

    public Snake(Bitmap snake_model, GameView gameView, int max_x, int max_y, int obstacle_x) {
        this.snake_model = snake_model;
        this.gameView = gameView;
        this.max_x = max_x;
        this.max_y = max_y;
        this.snake_x = obstacle_x;
        this.snake_y = - snake_model.getHeight();
        random = new Random();
    }

    public static Snake clone(Snake snake) {
        return new Snake(snake.snake_model, snake.getGameView(),snake.max_x, snake.max_y,
                new Random().nextInt(snake.max_x)+snake.snake_model.getWidth());
    }

    private void updateObstacle() {
        snake_y += snake_speed;
    }

    public void obstacleIsOut() {
        if (snake_y >= max_y) {
            snake_y = -10;
            snake_x = random.nextInt(max_x - max_x / 3) +1;
        }
    }

    public void drawSnake(Canvas canvas) {
        updateObstacle();
        obstacleIsOut();
        canvas.drawBitmap(snake_model, snake_x, snake_y, null);
    }

    public int getSnake_x() {
        return snake_x;
    }

    public void setSnake_x(int snake_x) {
        this.snake_x = snake_x;
    }

    public int getSnake_y() {
        return snake_y;
    }

    public void setSnake_y(int snake_y) {
        this.snake_y = snake_y;
    }

    public Bitmap getSnake_model() {
        return snake_model;
    }

    public void setSnake_model(Bitmap snake_model) {
        this.snake_model = snake_model;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public boolean getIs_drawable() {
        return is_drawable;
    }

    public void setIs_drawable(boolean is_drawable) {
        this.is_drawable = is_drawable;
    }

    public int getMax_y() {
        return max_y;
    }

    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }
}
