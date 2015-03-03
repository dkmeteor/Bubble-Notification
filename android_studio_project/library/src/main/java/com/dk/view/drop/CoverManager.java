package com.dk.view.drop;

import java.lang.reflect.Field;

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
		}
		mDropCover.setStatusBarHeight(getStatusBarHeight(activity));
	}

	public void start(View target, float x, float y,
			DropCover.OnDragCompeteListener onDragCompeteListener) {
		if (mDropCover != null && mDropCover.getParent() == null) {
			mDropCover.setOnDragCompeteListener(onDragCompeteListener);
		} else {
			return;
		}

		mDest = drawViewToBitmap(target);
		target.setVisibility(View.INVISIBLE);
		mDropCover.setTarget(mDest);
		int[] locations = new int[2];
		target.getLocationOnScreen(locations);
		attachToWindow(target.getContext());
		mDropCover.init(locations[0], locations[1]);
	}

	public void update(float x, float y) {
		mDropCover.update(x, y);
	}

	public void finish(final View target, final float x, final float y) {
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
				mDropCover.finish(target, x, y);
				mDropCover.setOnDragCompeteListener(null);
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
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		if (mDropCover == null) {
			mDropCover = new DropCover(context);
		}

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
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

	/**
	 * please call it before animation start
	 * 
	 * Notice: the unit is frame.
	 * 
	 * @param maxDistance
	 */
	public void setExplosionTime(int lifeTime) {
		Particle.setLifeTime(lifeTime);
	}

	public void setMaxDragDistance(int maxDistance) {
		if (mDropCover != null) {
			mDropCover.setMaxDragDistance(maxDistance);
		}
	}

	public static int getStatusBarHeight(Activity activity) {
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
		return sbar;
	}
}
