package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.math.Rectangle;

public class BreakableWall {

    public Rectangle bounds;

    private int hp;

    private boolean broken;

    private TextureRegion currentFrame;

    private Animation<TextureRegion> breakAnimation;

    private float stateTime;

    private boolean playingBreakAnimation;
    private boolean justBroken;

    public BreakableWall(
        float x,
        float y,
        float width,
        float height,
        int hp,
        Texture sheet
    ) {

        this.bounds =
            new Rectangle(
                x,
                y,
                width,
                height
            );

        this.hp = hp;

        TextureRegion[][] regions =
            TextureRegion.split(
                sheet,
                256,
                381
            );

        TextureRegion[] frames =
            new TextureRegion[4];

        for (int i = 0; i < 4; i++) {

            frames[i] =
                regions[0][i];
        }

        breakAnimation =
            new Animation<>(
                0.12f,
                frames
            );

        currentFrame = frames[0];
    }

    public void hit() {

        if (
            broken
                ||
                playingBreakAnimation
        ) {
            return;
        }

        hp--;

        if (hp <= 0) {

            playingBreakAnimation = true;

            stateTime = 0f;
        }
    }

    public void update(float delta) {

        if (!playingBreakAnimation) {
            return;
        }

        stateTime += delta;

        currentFrame =
            breakAnimation.getKeyFrame(
                stateTime
            );

        if (
            breakAnimation.isAnimationFinished(
                stateTime
            )
                &&
                !broken
        ) {

            broken = true;

            justBroken = true;

            playingBreakAnimation = false;
        }
    }

    public void render(SpriteBatch batch) {

        if (broken) {
            return;
        }

        batch.draw(
            currentFrame,
            bounds.x,
            bounds.y,
            bounds.width,
            bounds.height
        );
    }

    public boolean isBroken() {
        return broken;
    }

    public boolean consumeJustBroken() {

        if (justBroken) {

            justBroken = false;
            return true;
        }

        return false;
    }

    public boolean isSolid() {
        return !broken;
    }
}


