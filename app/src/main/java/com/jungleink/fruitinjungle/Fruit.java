package com.jungleink.fruitinjungle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Fruit implements Cloneable {
    private int fruit_x;
    private int fruit_y;
    private int max_x;
    private int max_y;
    private Bitmap fruit_model;
    private int fruit_speed = 10;
    private Random random;
    private GameView gameView;
    private boolean is_drawable;

    public Fruit(Bitmap fruit_model, GameView gameView, int max_x, int max_y, int fruit_x) {
        this.fruit_model = fruit_model;
        this.gameView = gameView;
        this.max_x = max_x;
        this.max_y = max_y;
        this.fruit_x = fruit_x;
        this.fruit_y = -fruit_model.getHeight();
        random = new Random();
    }

    public static Fruit clone(Fruit fruit) {
        return new Fruit(fruit.fruit_model, fruit.getGameView(), fruit.max_x, fruit.max_y,
                new Random().nextInt(fruit.max_x) + fruit.fruit_model.getWidth());
    }


    private void updateFruit() {
        fruit_y += fruit_speed;
    }

    public void fruitIsOut() {
        if (fruit_y >= max_y) {
            fruit_y = -10;
            fruit_x = random.nextInt(max_x) + this.fruit_model.getWidth();
        }
    }

    public void drawFruit(Canvas canvas) {
        updateFruit();
        fruitIsOut();
        canvas.drawBitmap(fruit_model, fruit_x, fruit_y, null);
    }

    public int getMax_x() {
        return max_x;
    }

    public void setMax_x(int max_x) {
        this.max_x = max_x;
    }

    public int getMax_y() {
        return max_y;
    }

    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }

    public Bitmap getFruit_model() {
        return fruit_model;
    }

    public void setFruit_model(Bitmap fruit_model) {
        this.fruit_model = fruit_model;
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

    public int getFruit_x() {
        return fruit_x;
    }

    public void setFruit_x(int fruit_x) {
        this.fruit_x = fruit_x;
    }

    public int getFruit_y() {
        return fruit_y;
    }

    public void setFruit_y(int fruit_y) {
        this.fruit_y = fruit_y;
    }
}
