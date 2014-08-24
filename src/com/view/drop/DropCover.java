package com.view.drop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DropCover extends ImageView {
    private float mBaseX;
    private float mBaseY;

    private float mTargetX;
    private float mTargetY;

    private Bitmap mDest;
    private Paint mPaint;

    private float targetWidth;
    private float targetHeight;

    private float mStrokeWidth = 25;
    private boolean isDraw = true;
    private float mStatusBarHeight = 0;

    public DropCover(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
        mPaint = new Paint();
    }

    public DropCover(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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

    }

    public void setTarget(Bitmap dest) {
        mDest = dest;
        targetWidth = dest.getWidth();
        targetHeight = dest.getHeight();
    }

    public void init(float x, float y) {
        mBaseX = x+mDest.getWidth()/2;
        mBaseY = y-mStatusBarHeight-mDest.getHeight()/2;
        mTargetX = x;
        mTargetY = y;

        isDraw = true;
        invalidate();
    }

    public void update(float x, float y) {

        mTargetX = x;
        mTargetY = y - mStatusBarHeight;
        invalidate();
    }

    public void clear() {
        mBaseX = -1;
        mBaseY = -1;
        mTargetX = -1;
        mTargetY = -1;
        mDest = null;
    }

    public void finish(float x, float y) {
        clear();
        isDraw = false;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        mStatusBarHeight = statusBarHeight;
    }
}
