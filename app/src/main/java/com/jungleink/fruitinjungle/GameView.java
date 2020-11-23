package com.jungleink.fruitinjungle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private MainActivity mainActivity;
    private GameThread gameThread;
    private Timer timer;
    private int score;
    private int max_x;
    private int max_y;/*
    private float touchX;*/
    private boolean isTouch = false;
    private Timer fruitTimer;
    private Timer snakeTimer;
    private Bitmap[] background;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastAccelerometerUpdate;

    private Snake snake;
    private List<Snake> snakes;
    private Iterator<Snake> snakeIterator;

    private Bucket bucket;

    private Fruit fruit;
    private List<Fruit> fruits;
    private Iterator<Fruit> fruitIterator;

    private Bitmap life[];
    private int life_count;

    public GameView(Context context, int max_x, int max_y) {
        super(context);
        getHolder().addCallback(this);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        lastAccelerometerUpdate = 0;

        mainActivity = (MainActivity) context;
        this.max_x = max_x;
        this.max_y = max_y;
        score = 0;
        timer = new Timer();
        fruitTimer = new Timer();
        snakeTimer = new Timer();
        gameThread = new GameThread(this);
        //bucket
        bucket = new Bucket(scale_bitmap(R.drawable.bucket, 180, 180), this, max_x, max_y);
        //background
        background = new Bitmap[2];
        background[0] = scale_bitmap(R.drawable.background, max_x, max_y);
        //life
        life = new Bitmap[2];
        life[0] = scale_bitmap(R.drawable.heart, 65, 65);
        life[1] = scale_bitmap(R.drawable.heart_g, 65, 65);
        life_count = 3;
        //snake
        snake = new Snake(scale_bitmap(R.drawable.snake, 120, 120), this, max_x, max_y, max_x / 2);
        snakes = initSnakeList(snake, snakes);
        snakeIterator = snakes.iterator();
        //fruit
        fruit = new Fruit(scale_bitmap(R.drawable.fruit, 120, 120), this, max_x, max_y, max_x - 30);
        fruits = initFruitList(fruit, fruits);
        fruitIterator = fruits.iterator();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameThread.setRun(true);
                gameThread.start();
            }
        }, 100);
        scheduleSnakeTask();
        scheduleFruitTask();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void space_draw(Canvas canvas) {
        canvas.drawBitmap(background[0], 0, 0, null);


        drawSnakes(canvas);
        drawFruits(canvas);

        //Life
        for (int i = 0; i < life_count; i++) {
            int x = (int) (canvas.getWidth() - 350 + life[0].getWidth() * 1.5 * i);
            int y = 30;
            canvas.drawBitmap(life[0], x, y, null);
        }

        //snake collision
        for (Snake s : snakes) {
            if (s.getIs_drawable() == true) {
                if (Rect.intersects(getCollisionObject(bucket.getBucket_x(), bucket.getBucket_y(), bucket.getBucket_model())
                        , getCollisionObject(s.getSnake_x(), s.getSnake_y(), s.getSnake_model()))) {
                    life_count--;
                    s.setSnake_y(max_y + 30);
                    if (life_count == 0) {
                        mainActivity.startEndActivity(score);
                        gameThread.setRun(false);
                    }
                }
            }
        }
        //fruit collision
        for (Fruit f : fruits) {
            if (f.getIs_drawable() == true) {
                if (Rect.intersects(getCollisionObject(bucket.getBucket_x(), bucket.getBucket_y(), bucket.getBucket_model())
                        , getCollisionObject(f.getFruit_x(), f.getFruit_y(), f.getFruit_model()))) {
                    f.setFruit_y(max_y + 30);
                    score += 10;
                }
            }
        }
        if (bucket.getBucket_x()>=max_x-bucket.getBucket_model().getWidth()){
            bucket.setBucket_x(max_x- bucket.getBucket_model().getWidth());
        }else if (bucket.getBucket_x()<=0){
            bucket.setBucket_x(0);
        }
        bucket.drawBucket(canvas);
    }

    private void drawSnakes(Canvas canvas) {
        for (Snake s : snakes) {
            if (s.getSnake_y() >= s.getMax_y()) {
                s.setIs_drawable(false);
                s.setSnake_y(-50 - s.getSnake_model().getHeight());
            }
            if (s.getIs_drawable() == true) {
                s.drawSnake(canvas);
            }
        }
    }

    private void scheduleSnakeTask() {
        snakeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!snakeIterator.hasNext()) {
                    snakeIterator = snakes.iterator();
                } else {
                    snakeIterator.next().setIs_drawable(true);
                }
            }
        }, 200, 2000);
    }


    private void drawFruits(Canvas canvas) {
        for (Fruit f : fruits) {
            if (f.getFruit_y() >= f.getMax_y()) {
                f.setIs_drawable(false);
                f.setFruit_y(-50 - f.getFruit_model().getHeight());
            }
            if (f.getIs_drawable() == true) {
                f.drawFruit(canvas);
            }
        }
    }

    private void scheduleFruitTask() {
        fruitTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!fruitIterator.hasNext()) {
                    fruitIterator = fruits.iterator();
                } else {
                    fruitIterator.next().setIs_drawable(true);
                }
            }
        }, 200, 2000);
    }

    private Bitmap scale_bitmap(Integer img_id, int size_x, int size_y) {
        Bitmap buff = BitmapFactory.decodeResource(getResources(), img_id);
        Bitmap finalBitmap = Bitmap.createScaledBitmap(buff, size_x, size_y, false);
        return finalBitmap;
    }

    public Rect getCollisionObject(int left, int top, Bitmap object_to_colission) {
        return new Rect(left, top, left + object_to_colission.getWidth(), top + object_to_colission.getHeight());
    }

    private List<Fruit> initFruitList(Fruit initFruit, List<Fruit> fruitList) {
        fruitList = new ArrayList<>();
        fruitList.add(Fruit.clone(initFruit));
        fruitList.add(Fruit.clone(initFruit));
        fruitList.add(Fruit.clone(initFruit));
        fruitList.add(Fruit.clone(initFruit));
        return fruitList;
    }

    private List<Snake> initSnakeList(Snake initSnake, List<Snake> snakeList) {
        snakeList = new ArrayList<>();
        snakeList.add(Snake.clone(initSnake));
        snakeList.add(Snake.clone(initSnake));
        snakeList.add(Snake.clone(initSnake));
        snakeList.add(Snake.clone(initSnake));
        return snakeList;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (System.currentTimeMillis() - lastAccelerometerUpdate > 50) {
                if (event.values[0] > 0) {
                    bucket.moveLeft(true);
                    bucket.moveRight(false);
                } else {
                    bucket.moveRight(true);
                    bucket.moveLeft(false);
                }
                lastAccelerometerUpdate = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
