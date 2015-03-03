package com.dk.view.drop;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * The thead will finish when you called setRunning(false) or Explosion ended.
 * 
 * @author Peter.Ding
 * 
 */
public class ExplosionUpdateThread extends Thread {
    private SurfaceHolder mHolder;
    private DropCover mDropCover;
    private boolean isRunning = false;

    public ExplosionUpdateThread(SurfaceHolder holder, DropCover dropCover) {
        mHolder = holder;
        mDropCover = dropCover;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        boolean isAlive = true;
        while (isRunning && isAlive) {
            Canvas canvas = mHolder.lockCanvas();
            if (canvas != null) {
                isAlive = mDropCover.render(canvas);
                mHolder.unlockCanvasAndPost(canvas);
                mDropCover.update();
            }
        }

        mDropCover.clearViews();
    }
}
