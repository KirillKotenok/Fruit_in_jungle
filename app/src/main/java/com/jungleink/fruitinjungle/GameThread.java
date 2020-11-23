package com.jungleink.fruitinjungle;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private GameView gameView;
    private static boolean is_run = false;

    public GameThread(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void run() {
        while (is_run) {
            SurfaceHolder holder = gameView.getHolder();
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                gameView.space_draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void setRun(boolean run){
        this.is_run = run;
    }
}
