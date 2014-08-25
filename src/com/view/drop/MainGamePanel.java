//package com.view.drop;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PixelFormat;
//import android.graphics.Rect;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
//
//    private static final String TAG = MainGamePanel.class.getSimpleName();
//
//    private static final int EXPLOSION_SIZE = 200;
//
//    private MainThread thread;
//    private Explosion explosion;
//
//    // the fps to be displayed
//    private String avgFps;
//
//    public void setAvgFps(String avgFps) {
//        this.avgFps = avgFps;
//    }
//
//    public MainGamePanel(Context context) {
//        super(context);
//        // adding the callback (this) to the surface holder to intercept events
//        this.setBackgroundColor(Color.TRANSPARENT); // To make canvas
//                                                    // transparent
//        this.setZOrderOnTop(true); // To make canvas transparent
//        getHolder().setFormat(PixelFormat.TRANSPARENT); // To make canvas
//                                                        // transparent!
//
//        getHolder().addCallback(this);
//
//        // make the GamePanel focusable so it can handle events
//        setFocusable(true);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // create the game loop thread
//        thread = new MainThread(getHolder(), this);
//
//        // at this point the surface is created and
//        // we can safely start the game loop
//        thread.setRunning(true);
//        thread.start();
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        Log.d(TAG, "Surface is being destroyed");
//        // tell the thread to shut down and wait for it to finish
//        // this is a clean shutdown
//        boolean retry = true;
//        while (retry) {
//            try {
//                thread.setRunning(false);
//                thread.join();
//                retry = false;
//            } catch (InterruptedException e) {
//                // try again shutting down the thread
//            }
//        }
//        Log.d(TAG, "Thread was shut down cleanly");
//    }
//
//    // @Override
//    // public boolean onTouchEvent(MotionEvent event) {
//    // if (event.getAction() == MotionEvent.ACTION_DOWN) {
//    // if (explosion == null || explosion.getState() == Explosion.STATE_DEAD) {
//    // explosion = new Explosion(EXPLOSION_SIZE, (int) event.getX(), (int)
//    // event.getY());
//    // }
//    // }
//    // return true;
//    // }
//
//    public void initExplosion(float x, float y) {
//        if (explosion == null || explosion.getState() == Explosion.STATE_DEAD) {
//            explosion = new Explosion(EXPLOSION_SIZE, (int) x, (int) y);
//        }
//    }
//
//    public void render(Canvas canvas) {
//
//        canvas.drawColor(Color.argb(0, 0, 0, 0)); // To make canvas transparent
//
//        // render explosions
//        if (explosion != null) {
//            explosion.draw(canvas);
//        }
//
//        // display fps
//        // displayFps(canvas, avgFps);
//
//        // display border
//        /*
//         * Paint paint = new Paint(); paint.setColor(Color.GREEN);
//         * canvas.drawLines(new float[]{ 0,0, canvas.getWidth()-1,0,
//         * canvas.getWidth()-1,0, canvas.getWidth()-1,canvas.getHeight()-1,
//         * canvas.getWidth()-1,canvas.getHeight()-1, 0,canvas.getHeight()-1,
//         * 0,canvas.getHeight()-1, 0,0 }, paint);
//         */
//    }
//
//    /**
//     * This is the game update method. It iterates through all the objects and
//     * calls their update method if they have one or calls specific engine's
//     * update method.
//     */
//    public void update() {
//        // update explosions
//        if (explosion != null && explosion.isAlive()) {
//            explosion.update(getHolder().getSurfaceFrame());
//        }
//    }
//
//    private void displayFps(Canvas canvas, String fps) {
//        if (canvas != null && fps != null) {
//            Paint paint = new Paint();
//            paint.setARGB(255, 255, 255, 255);
//            canvas.drawText(fps, this.getWidth() - 50, 20, paint);
//        }
//    }
//
//}
