package com.yourgirlseta.hollowKnight.model.effect;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HitEffect {

    private Animation<TextureRegion> animation;

    private float stateTime = 0f;

    private float x;
    private float y;

    private boolean finished = false;

    public HitEffect(
        Animation<TextureRegion> animation,
        float x,
        float y
    ) {

        this.animation = animation;

        this.x = x;
        this.y = y;
    }

    public void update(float delta) {

        stateTime += delta;

        if (
            animation.isAnimationFinished(
                stateTime
            )
        ) {

            finished = true;
        }
    }

    public void render(
        SpriteBatch batch
    ) {

        TextureRegion frame =
            animation.getKeyFrame(
                stateTime
            );

        batch.draw(
            frame,
            x,
            y,
            80,
            80
        );
    }

    public boolean isFinished() {

        return finished;
    }
}
