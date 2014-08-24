package com.view.drop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WaterDrop extends RelativeLayout {
    private Paint mPaint;
    private TextView mTextView;

    public WaterDrop(Context context) {
        super(context);
        init();
    }

    public WaterDrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTextView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTextView.setText("99");
        mTextView.setTextColor(0xffffffff);
        mTextView.setLayoutParams(params);
        addView(mTextView);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }

        mPaint.setColor(0xffff0000);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            CoverManager.getInstance().start(this, event.getRawX(), event.getRawY());
            break;
        case MotionEvent.ACTION_MOVE:
            CoverManager.getInstance().update(event.getRawX(), event.getRawY());
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            CoverManager.getInstance().finish(this, event.getRawX(), event.getRawY());
            break;
        }

        return true;
    }
}
