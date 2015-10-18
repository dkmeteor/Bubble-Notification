package com.dk.view.drop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;

/**
 * Created by DK on 2015/10/18.
 */
public class GifUpdateThread extends Thread implements RenderActionInterface {
    private SurfaceHolder mHolder;
    private DropCover mDropCover;
    private boolean isRunning = false;

    public GifUpdateThread(SurfaceHolder holder, DropCover dropCover) {
        mHolder = holder;
        mDropCover = dropCover;
    }

    public void actionStart() {
        isRunning = true;
        start();
    }

    public void actionStop() {
        this.isRunning = false;
    }
    @Override
    public void run() {
        boolean isAlive = true;
        long start =System.nanoTime();
        while (isRunning && isAlive) {
            Canvas canvas = mHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if (canvas != null) {

                long now = android.os.SystemClock.uptimeMillis();

                //TODO
                //Refactor: GifRender move to here.

                if ( mDropCover.mGifRender.mMovieStart != 0 && now - mDropCover.mGifRender.mMovieStart > mDropCover.mGifRender.getMovie().duration()) {
                    actionStop();
                }
                mDropCover.updateGif(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
        mDropCover.clearViews();
    }
}