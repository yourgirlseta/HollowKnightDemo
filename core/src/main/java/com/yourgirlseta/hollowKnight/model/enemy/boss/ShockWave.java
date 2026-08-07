package com.yourgirlseta.hollowKnight.model.enemy.boss;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.yourgirlseta.hollowKnight.model.character.Player;

public class ShockWave {

    private final Rectangle bounds;
    private float speed = 250f;
    private final int direction;
    private boolean remove;
    private final Animation<TextureRegion> animation;
    private float stateTime = 0f;

    private static final float VISUAL_WIDTH = 120f;
    private static final float VISUAL_HEIGHT = 120f;

    public float getVisualWidth() { return VISUAL_WIDTH; }
    public float getVisualHeight() { return VISUAL_HEIGHT; }

    public ShockWave(float x, float y, int direction, Animation<TextureRegion> animation) {

        this.direction = direction;
        this.animation = animation;
        bounds = new Rectangle(
            x,
            y,
            30,
            10
        );
    }

    public void update(float delta) {
        bounds.x += speed * direction * delta;
        speed += 50f * delta;
        stateTime += delta;
        if (bounds.x < -500 || bounds.x > 20000) {
            remove = true;
        }
    }

    public int getDirection() {
        return direction;
    }

    public void checkHit(Player player) {

        if (remove) return;

        if (bounds.overlaps(player.getBounds())) {

            player.takeHit(bounds.x);

            remove = true;
        }
    }
    public TextureRegion getCurrentFrame() {
        return animation.getKeyFrame(stateTime, true);
    }


    public Rectangle getBounds() {
        return bounds;
    }

    public boolean shouldRemove() {
        return remove;
    }
}
