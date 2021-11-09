package com.seana02;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle {
    private float x;
    private float y;
    private float width;
    private float height;
    public float dy;

    public Paddle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        dy = 0;
    }

    public void update(float dt) {
        if (dy < 0) {
            y = Math.max(0, y + dy * dt);
        } else {
            y = Math.min(Main.VIRTUAL_HEIGHT - height, y + dy * dt);
        }
    }

    public void render(ShapeRenderer sr) {
        sr.rect(x, y, width, height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

}
