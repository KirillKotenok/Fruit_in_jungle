package com.jungleink.fruitinjungle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bucket {

    private int bucket_x;
    private int bucket_y;
    private int max_x;
    private int max_y;
    private Bitmap bucket_model;
    private boolean bucket_left;
    private boolean bucket_right;

    private GameView gameView;

    public Bucket(Bitmap bucket_model, GameView gameView, int max_x, int max_y) {
        this.bucket_model = bucket_model;
        this.gameView = gameView;
        this.max_x = max_x;
        this.max_y = max_y;

        this.bucket_left = false;
        this.bucket_right = false;
        this.bucket_x = max_x / 2;
        this.bucket_y = max_y - bucket_model.getHeight() - 10;
    }

    public void drawBucket(Canvas canvas) {
        if (bucket_right==true){
            bucket_x+=10;
        }else if (bucket_left==true){
            bucket_x-=10;
        }
        canvas.drawBitmap(bucket_model, bucket_x, bucket_y, null);
    }

    public void setBucket_x(int bucket_x) {
        this.bucket_x = bucket_x;
    }

    public int getBucket_x() {
        return bucket_x;
    }

    public Bitmap getBucket_model() {
        return bucket_model;
    }

    public void setBucket_model(Bitmap bucket_model) {
        this.bucket_model = bucket_model;
    }

    public int getBucket_y() {
        return bucket_y;
    }

    public void setBucket_y(int bucket_y) {
        this.bucket_y = bucket_y;
    }

    public void moveLeft(boolean res) {
        bucket_left = res;
    }

    public void moveRight(boolean res) {
        bucket_right = res;
    }
}
