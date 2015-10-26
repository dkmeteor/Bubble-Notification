package com.dk.view.drop;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class CoverManager {
    private static final int EXPLOSION_SIZE = 200;
    private int mMaxDistance = 100;

    private static CoverManager mCoverManager;
    private static Bitmap mDest;
    private DropCover mDropCover;
    private WindowManager mWindowManager;

    private RenderActionInterface mThread;
    private Explosion mExplosion;
    private int mStatusBarHeight = 0;

    private int mResourceId = -1;

    public interface OnDragCompeteListener {
        void onDragComplete();
    }

    private OnDragCompeteListener mOnDragCompeteListener;

    private CoverManager() {

    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public static CoverManager getInstance() {
        if (mCoverManager == null) {
            mCoverManager = new CoverManager();
        }
        return mCoverManager;
    }

    public void init(Activity activity) {
        if (mDropCover == null) {
            mDropCover = new DropCover(activity);
            mWindowManager = activity.getWindowManager();
            mDropCover.setStatusBarHeight(getStatusBarHeight(activity));
            getStatusBarHeight(activity);
        }
    }

    public void setEffectResource(int resourceId) {
        mResourceId = resourceId;
    }

    public void start(View target, float x, float y,
                      OnDragCompeteListener onDragCompeteListener) {
        mOnDragCompeteListener = onDragCompeteListener;
        if (!(mDropCover != null && mDropCover.getParent() == null)) {
            return;
        }

        mDest = drawViewToBitmap(target);
        target.setVisibility(View.INVISIBLE);
        mDropCover.setTarget(mDest);
        final int[] locations = new int[2];
        target.getLocationOnScreen(locations);
        attachToWindow(target.getContext());
        mDropCover.init(locations[0], locations[1], x, y);
    }

    public void update(float x, float y) {
        mDropCover.update(x, y);
    }

    public void finishDrag(final View target, final float x, final float y) {
        /**
         *
         * if click very quick.
         * Drop may be finished before first frame.
         * It will cause SurfaceView render error.
         *
         */
        target.postDelayed(new Runnable() {

            @Override
            public void run() {
                double distance = mDropCover.stopDrag(target, x, y, mResourceId);
                startEffect(distance, x, y);
            }
        }, 30);

    }

    private Bitmap drawViewToBitmap(View view) {
        if (mDropCover == null) {
            mDropCover = new DropCover(view.getContext());
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (mDest == null || mDest.getWidth() != width
                || mDest.getHeight() != height) {
            mDest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas c = new Canvas(mDest);
        view.draw(c);
        return mDest;
    }

    private void attachToWindow(Context context) {
        if (mDropCover == null) {
            mDropCover = new DropCover(context);
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWindowManager.addView(mDropCover, params);
    }

    public boolean isRunning() {
        if (mDropCover == null) {
            return false;
        } else if (mDropCover.getParent() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void startEffect(double distance,float x ,float y) {

        if (distance > mMaxDistance) {
            if (mOnDragCompeteListener != null)
                mOnDragCompeteListener.onDragComplete();

            if (mResourceId > 0) {
//                mThread = new GifUpdateThread(mDropCover.getTargetX(),mDropCover.getTargetY(),mDropCover.getHolder(), mDropCover.getContext().getApplicationContext(),mResourceId);

                mThread = new GifUpdateThread(x, y - mStatusBarHeight,mDropCover.getHolder(), mDropCover.getContext().getApplicationContext(),mResourceId);
            } else {
                initExplosion(x, y - mStatusBarHeight);
                mThread = new ExplosionUpdateThread(mDropCover.getHolder(), mDropCover);
            }
            mThread.actionStart();
        } else {
            if (mDropCover.getParent() != null) {
                getWindowManager().removeView(mDropCover);
            }
//            target.setVisibility(View.VISIBLE);
        }
    }

    public void stopEffect() {
        if (mThread != null) {
            mThread.actionStop();
            mThread = null;
        }

    }


    public void setEffectDuration(int lifeTime) {
        Particle.setLifeTime(lifeTime);
    }

    public void setMaxDragDistance(int maxDistance) {
        if (mDropCover != null) {
            mDropCover.setMaxDragDistance(maxDistance);
        }
    }


    /**
     * init the explosion whit start position
     *
     * @param x
     * @param y
     */
    public void initExplosion(float x, float y) {
        if (mExplosion == null || mExplosion.getState() == Explosion.STATE_DEAD) {
            mExplosion = new Explosion(EXPLOSION_SIZE, (int) x, (int) y);
        }
    }

    /**
     * call it to draw explosion
     *
     * @param canvas
     * @return isAlive
     */
    public boolean render(Canvas canvas) {
        boolean isAlive = false;
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.argb(0, 0, 0, 0)); // To make canvas transparent
        // render explosions
        if (mExplosion != null) {
            isAlive = mExplosion.draw(canvas);
        }
        return isAlive;
    }

    /**
     * update explosion
     */
    public void updateExplosion() {
        // update explosions
        if (mExplosion != null && mExplosion.isAlive()) {
            mExplosion.update(mDropCover.getHolder().getSurfaceFrame());
        }
    }


    public void removeViews(){
        if(mDropCover!=null && mDropCover.getParent()!=null)
            getWindowManager().removeView(mDropCover);
    }

    /**
     * return statusbar height
     *
     * @param activity
     * @return
     */
    public int getStatusBarHeight(Activity activity) {

        if (mStatusBarHeight == 0) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0, sbar = 38;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                sbar = activity.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            mStatusBarHeight = sbar;
            return sbar;
        } else {
            return mStatusBarHeight;
        }
    }

}
