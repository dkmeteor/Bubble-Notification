package com.dk.view.drop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Particle {

    public static final int STATE_ALIVE = 0; // particle is alive
    public static final int STATE_DEAD = 1; // particle is dead

    public static final int DEFAULT_LIFETIME = 100; // play with this
    public static final int MAX_DIMENSION = 5; // the maximum width or height
    public static final int MAX_SPEED = 10; // maximum speed (per update)

    private int state; // particle is alive or dead
    private float widht; // width of the particle
    private float height; // height of the particle
    private float x, y; // horizontal and vertical position
    private double xv, yv; // vertical and horizontal velocity
    private int age; // current age of the particle
    private static int lifetime; // particle dies when it reaches this value
    private int color; // the color of the particle
    private Paint paint; // internal use to avoid instantiation

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getWidht() {
        return widht;
    }

    public void setWidht(float widht) {
        this.widht = widht;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getXv() {
        return xv;
    }

    public void setXv(double xv) {
        this.xv = xv;
    }

    public double getYv() {
        return yv;
    }

    public void setYv(double yv) {
        this.yv = yv;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    // helper methods -------------------------
    public boolean isAlive() {
        return this.state == STATE_ALIVE;
    }

    public boolean isDead() {
        return this.state == STATE_DEAD;
    }

    public Particle(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = Particle.STATE_ALIVE;
        this.widht = rndInt(1, MAX_DIMENSION);
        this.height = this.widht;// this.height = rnd(1, MAX_DIMENSION);
        this.lifetime = DEFAULT_LIFETIME;
        this.age = 0;
        this.xv = (rndDbl(0, MAX_SPEED * 2) - MAX_SPEED);
        this.yv = (rndDbl(0, MAX_SPEED * 2) - MAX_SPEED);
        // smoothing out the diagonal speed
        if (xv * xv + yv * yv > MAX_SPEED * MAX_SPEED) {
            xv *= 0.7;
            yv *= 0.7;
        }
        this.color = Color.argb(255, rndInt(0, 255), rndInt(0, 255), rndInt(0, 255));
        this.paint = new Paint(this.color);
    }

    /**
     * Resets the particle
     * 
     * @param x
     * @param y
     */
    public void reset(float x, float y) {
        this.state = Particle.STATE_ALIVE;
        this.x = x;
        this.y = y;
        this.age = 0;
    }

    // Return an integer that ranges from min inclusive to max inclusive.
    static int rndInt(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

    static double rndDbl(double min, double max) {
        return min + (max - min) * Math.random();
    }

    public static void setLifeTime(int lifeTime) {
        lifetime = lifeTime;
    }

    public void update() {
        if (this.state != STATE_DEAD) {
            this.x += this.xv;
            this.y += this.yv;

            // extract alpha
            int a = this.color >>> 24;
            a -= 4; // fade out
            if (a <= 0) { // if reached transparency kill the particle
                this.state = STATE_DEAD;
            } else {
                this.color = (this.color & 0x00ffffff) + (a << 24); // set the
                                                                    // new alpha
                this.paint.setAlpha(a);
                this.age++; // increase the age of the particle// this.widht *=
                            // 1.05;// this.height *= 1.05;
            }
            if (this.age >= this.lifetime) { // reached the end if its life
                this.state = STATE_DEAD;
            }

            // http://lab.polygonal.de/2007/05/10/bitwise-gems-fast-integer-math/
            // 32bit// var color:uint = 0xff336699;// var a:uint = color >>>
            // 24;// var r:uint = color >>> 16 & 0xFF;// var g:uint = color >>>
            // 8 & 0xFF;// var b:uint = color & 0xFF;

        }
    }

    public void update(Rect container) {
        // update with collision
        if (this.isAlive()) {
            if (this.x <= container.left || this.x >= container.right - this.widht) {
                this.xv *= -1;
            }
            // Bottom is 480 and top is 0 !!!
            if (this.y <= container.top || this.y >= container.bottom - this.height) {
                this.yv *= -1;
            }
        }
        update();
    }

    public void draw(Canvas canvas) {// paint.setARGB(255, 128, 255, 50);
        paint.setColor(this.color);
        canvas.drawRect(this.x, this.y, this.x + this.widht, this.y + this.height, paint);// canvas.drawCircle(x,
                                                                                          // y,
                                                                                          // widht,
                                                                                          // paint);
    }

}
