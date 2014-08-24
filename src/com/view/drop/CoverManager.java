package com.view.drop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

public class CoverManager {
    private static CoverManager mCoverManager;
    private static Bitmap mDest;
    private DropCover mDropCover;
    private WindowManager mWindowManager;

    private CoverManager() {

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
        }
        mDropCover.setStatusBarHeight(ViewUtils.getStatusBarHeight(activity));
    }

    public void start(View target, float x, float y) {
        mDest = drawViewToBitmap(target);
        target.setVisibility(View.INVISIBLE);
        mDropCover.setTarget(mDest);
        int[] locations = new int[2];
        target.getLocationOnScreen(locations);
        mDropCover.init(locations[0], locations[1]);

        attachToWindow(target.getContext());
    }

    public void update(float x, float y) {
        mDropCover.update(x, y);
    }

    public void finish(View target, float x, float y) {
        mDropCover.finish(x, y);

        if (mDropCover.getParent() != null) {
            mWindowManager.removeView(mDropCover);
        }
        target.setVisibility(View.VISIBLE);
    }

    private Bitmap drawViewToBitmap(View view) {
        if (mDropCover == null) {
            mDropCover = new DropCover(view.getContext());
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (mDest == null || mDest.getWidth() != width || mDest.getHeight() != height) {
            mDest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas c = new Canvas(mDest);
        view.draw(c);
        return mDest;
    }

    private void attachToWindow(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (mDropCover == null) {
            mDropCover = new DropCover(context);
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;
        mWindowManager.addView(mDropCover, params);
    }
}
