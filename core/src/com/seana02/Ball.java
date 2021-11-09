package com.seana02;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {
    private float x;
    private float y;
    private float width;
    private float height;
    private float dx;
    private float dy;


    public Ball (float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.dx = ((int) Math.random()*2) == 1 ? 100 : -100;
		this.dy = (int) Math.random() * 50;
        
    }

    public boolean collides(Paddle p) {
        if (x > p.getX() + p.getWidth() || p.getX() > x + width) {
            return false;
        }
        if (y > p.getY() + p.getHeight() || p.getY() > y + height) {
            return false;
        }
        return true;
    }

    public void reset() {
        x = Main.VIRTUAL_WIDTH/2 - 2;
        y = Main.VIRTUAL_HEIGHT/2 - 2;
    }

    public void update(float dt) {
        x += dx * dt;
        y += dy * dt;
    }

    public void render(ShapeRenderer sr) {
        sr.rect(x, y, width, height);
    }

    public void setDX(float newDX) { dx = newDX; }
    public void setDY(float newDY) { dy = newDY; }
    public float getDX() { return dx; }
    public float getDY() { return dy; }

    public void setX(float newX) { x = newX; }
    public void setY(float newY) { y = newY; }
    public float getX() { return x; }
    public float getY() { return y; }
}
