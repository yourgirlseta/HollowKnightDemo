package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RubbleParticle {

    private float x, y;
    private float velocityX, velocityY;
    private float stateTime = 0f;
    private float life;
    private float maxLife;
    private static final float GRAVITY = -900f;
    private Animation<TextureRegion> animation;

    public RubbleParticle(
        Animation<TextureRegion> animation,
        float x, float y,
        float velocityX, float velocityY,
        float maxLife
    ) {
        this.animation = animation;
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.maxLife = maxLife;
        this.life = maxLife;
    }

    public void update(float delta) {
        velocityY += GRAVITY * delta;

        x += velocityX * delta;
        y += velocityY * delta;

        stateTime += delta;
        life -= delta;
    }

    public boolean isFinished() {
        return life <= 0f;
    }

    public void render(SpriteBatch batch) {
        TextureRegion frame = animation.getKeyFrame(stateTime, true);

        float alpha = Math.max(0f, life / maxLife);
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(frame, x, y, 16, 16);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}

