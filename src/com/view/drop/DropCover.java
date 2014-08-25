package com.view.drop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DropCover extends SurfaceView implements SurfaceHolder.Callback {

    private static final int EXPLOSION_SIZE = 200;

    private MainThread thread;
    private Explosion explosion;

    private String avgFps;

    private float mBaseX;
    private float mBaseY;

    private float mTargetX;
    private float mTargetY;

    private Bitmap mDest;
    private Paint mPaint = new Paint();

    private float targetWidth;
    private float targetHeight;

    private float mStrokeWidth = 25;
    private boolean isDraw = true;
    private float mStatusBarHeight = 0;
    private OnDragCompeteListener mOnDragCompeteListener;

    public interface OnDragCompeteListener {
        void onDrag();
    }

    public DropCover(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to
        // intercept events
        this.setBackgroundColor(Color.TRANSPARENT); // To make canvas
        // transparent
        this.setZOrderOnTop(true); // To make canvas transparent
        getHolder().setFormat(PixelFormat.TRANSPARENT); // To make canvas
        // transparent!

        getHolder().addCallback(this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    private void drawDrop() {
        Canvas canvas = getHolder().lockCanvas();

        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

        if (isDraw) {
            double distance = Math.sqrt(Math.pow(mBaseX - mTargetX, 2) + Math.pow(mBaseY - mTargetY, 2));
            mPaint.setColor(0xffff0000);
            canvas.drawCircle(mBaseX, mBaseY, mStrokeWidth, mPaint);
            if (distance < 200) {
                mStrokeWidth = (float) ((1 - distance / 200) * 25);
                mPaint.setStrokeWidth(mStrokeWidth);
                canvas.drawLine(mBaseX, mBaseY, mTargetX + targetWidth / 2, mTargetY + targetHeight / 2, mPaint);
            }
            canvas.drawBitmap(mDest, mTargetX, mTargetY, mPaint);
        }
        getHolder().unlockCanvasAndPost(canvas);
    }

    public void setTarget(Bitmap dest) {
        mDest = dest;
        targetWidth = dest.getWidth();
        targetHeight = dest.getHeight();
    }

    public void init(float x, float y) {
        mBaseX = x + mDest.getWidth() / 2;
        mBaseY = y - mDest.getWidth() / 2;
        mTargetX = x;
        mTargetY = y;

        isDraw = true;
    }

    public void update(float x, float y) {

        mTargetX = x;
        mTargetY = y - mStatusBarHeight;
        drawDrop();
    }

    public void clear() {
        mBaseX = -1;
        mBaseY = -1;
        mTargetX = -1;
        mTargetY = -1;
        mDest = null;
    }

    public void finish(float x, float y) {
        double distance = Math.sqrt(Math.pow(mBaseX - mTargetX, 2) + Math.pow(mBaseY - mTargetY, 2));
        if (distance > 200 && mOnDragCompeteListener != null) {
            mOnDragCompeteListener.onDrag();
        }


        clear();
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        getHolder().unlockCanvasAndPost(canvas);
        

        initExplosion(x, y);

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

        isDraw = false;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        mStatusBarHeight = statusBarHeight;
    }

    public void setOnDragCompeteListener(OnDragCompeteListener onDragCompeteListener) {
        mOnDragCompeteListener = onDragCompeteListener;
    }

    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // create the game loop thread

        // at this point the surface is created and
        // we can safely start the game loop

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // if (event.getAction() == MotionEvent.ACTION_DOWN) {
    // if (explosion == null || explosion.getState() == Explosion.STATE_DEAD) {
    // explosion = new Explosion(EXPLOSION_SIZE, (int) event.getX(), (int)
    // event.getY());
    // }
    // }
    // return true;
    // }

    public void initExplosion(float x, float y) {
        if (explosion == null || explosion.getState() == Explosion.STATE_DEAD) {
            explosion = new Explosion(EXPLOSION_SIZE, (int) x, (int) y);
        }
    }

    public void render(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        canvas.drawColor(Color.argb(0, 0, 0, 0)); // To make canvas transparent
        // render explosions
        if (explosion != null) {
            explosion.draw(canvas);
        }

        // display fps
        // displayFps(canvas, avgFps);

        // display border
        /*
         * Paint paint = new Paint(); paint.setColor(Color.GREEN);
         * canvas.drawLines(new float[]{ 0,0, canvas.getWidth()-1,0,
         * canvas.getWidth()-1,0, canvas.getWidth()-1,canvas.getHeight()-1,
         * canvas.getWidth()-1,canvas.getHeight()-1, 0,canvas.getHeight()-1,
         * 0,canvas.getHeight()-1, 0,0 }, paint);
         */
    }

    /**
     * This is the game update method. It iterates through all the objects and
     * calls their update method if they have one or calls specific engine's
     * update method.
     */
    public void update() {
        // update explosions
        if (explosion != null && explosion.isAlive()) {
            explosion.update(getHolder().getSurfaceFrame());
        }
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255, 255, 255, 255);
            canvas.drawText(fps, this.getWidth() - 50, 20, paint);
        }
    }
}
