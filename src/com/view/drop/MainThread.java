package com.view.drop;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private SurfaceHolder mHolder;
    private DropCover mDropCover;
    private boolean isRunning = false;

    public MainThread(SurfaceHolder holder, DropCover dropCover) {
        mHolder = holder;
        mDropCover = dropCover;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        while (true) {
            Canvas canvas = mHolder.lockCanvas();
            if (canvas != null) {
                mDropCover.render(canvas);
                mHolder.unlockCanvasAndPost(canvas);
                mDropCover.update();
            }
        }
    }
}
