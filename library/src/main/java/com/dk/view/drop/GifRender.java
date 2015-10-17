package com.dk.view.drop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;

/**
 * Created by DK on 2015/10/17.
 * <p/>
 * <p/>
 * See:
 * <p/>
 * https://github.com/sbakhtiarov/gif-movie-view
 */
public class GifRender {
    private static final int DEFAULT_MOVIEW_DURATION = 1000;

    private int mMovieResourceId;
    private Movie mMovie;

    private long mMovieStart;
    private int mCurrentAnimationTime = 0;


    /**
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale;

    /**
     * Scaled movie frames width and height.
     */
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;

    private volatile boolean mPaused = false;
    private Context mContext;

    public GifRender(Context context) {
        if (context instanceof Activity)
            mContext = ((Activity) context).getApplication();
        else
            mContext = context;
    }


    private void setViewAttributes(Context context, int resId) {

        mMovieResourceId = resId;

        if (mMovieResourceId != -1) {
            mMovie = Movie.decodeStream(context.getResources().openRawResource(mMovieResourceId));
        }
    }

    public void setMovieResource(int movieResId) {
        this.mMovieResourceId = movieResId;
        mMovie = Movie.decodeStream(mContext.getResources().openRawResource(mMovieResourceId));
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;

        /**
         * Calculate new movie start time, so that it resumes from the same
         * frame.
         */
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;
        }
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    public void measure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mMovie != null) {
            int movieWidth = mMovie.width();
            int movieHeight = mMovie.height();

			/*
             * Calculate horizontal scaling
			 */
            float scaleH = 1f;

            int maximumWidth = widthMeasureSpec;
            if (movieWidth > maximumWidth) {
                scaleH = (float) movieWidth / (float) maximumWidth;
            }

			/*
             * calculate vertical scaling
			 */
            float scaleW = 1f;

            int maximumHeight = heightMeasureSpec;
            if (movieHeight > maximumHeight) {
                scaleW = (float) movieHeight / (float) maximumHeight;
            }

			/*
             * calculate overall scale
			 */
            mScale = 1f / Math.max(scaleH, scaleW);

            mMeasuredMovieWidth = (int) (movieWidth * mScale);
            mMeasuredMovieHeight = (int) (movieHeight * mScale);

        } else {

        }
    }


    public void draw(Canvas canvas, float x, float y) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas, x, y);
            } else {
                drawMovieFrame(canvas, x, y);
            }
        }
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int dur = mMovie.duration();
        if (dur == 0) {
            dur = DEFAULT_MOVIEW_DURATION;
        }
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas, float x, float y) {

        mMovie.setTime(mCurrentAnimationTime);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, x / mScale, y / mScale);
        canvas.restore();
    }

}
