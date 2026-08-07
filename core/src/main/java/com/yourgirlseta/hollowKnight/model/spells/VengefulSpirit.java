package com.yourgirlseta.hollowKnight.model.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectSet;
import com.yourgirlseta.hollowKnight.model.charms.CharmManager;
import com.yourgirlseta.hollowKnight.model.enemy.Enemy;


public class VengefulSpirit {

    private Rectangle bounds;

    private int direction;

    private float speed = 900f;

    private float stateTime = 0f;

    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> voidHeartAnimation;
    private static Animation<TextureRegion> sharedAnimation;
    private static Animation<TextureRegion> sharedVoidHeartAnimation;


    private final CharmManager charmManager;

    private ObjectSet<Enemy>
        hitEnemies =
        new ObjectSet<>();

    public VengefulSpirit(
        float x,
        float y,
        int direction,
        CharmManager charmManager
    ) {

        this.direction = direction;
        this.charmManager = charmManager;

        bounds =
            new Rectangle(
                x,
                y,
                120,
                60
            );

        loadAnimation();
    }

    private void loadAnimation() {
        if (sharedAnimation == null) {
            Texture sheet = new Texture("animation/projectile/SoulBall.png");
            sharedAnimation = buildAnimation(sheet, 317, 143, 4, 0.06f);

            Texture voidHeartSheet = new Texture("animation/projectile/ShadowBall.png");
            sharedVoidHeartAnimation = buildAnimation(voidHeartSheet, 504, 157, 4, 0.06f);
        }
        this.animation = sharedAnimation;
        this.voidHeartAnimation = sharedVoidHeartAnimation;
    }


    private Animation<TextureRegion> buildAnimation(
        Texture sheet, int frameWidth, int frameHeight, int frameCount, float frameDuration
    ) {
        TextureRegion[][] temp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[frameCount];
        int index = 0;
        for (int r = 0; r < temp.length; r++) {
            for (int c = 0; c < temp[r].length; c++) {
                if (index < frameCount) {
                    frames[index++] = temp[r][c];
                }
            }
        }
        Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }


    public void update(float delta) {
        stateTime += delta;
        bounds.x += speed * direction * delta;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean hasHit(Enemy enemy) {
        return hitEnemies.contains(enemy);
    }

    public void markHit(Enemy enemy) {
        hitEnemies.add(enemy);
    }

    public void render(SpriteBatch batch) {

        Animation<TextureRegion> activeAnimation =
            charmManager.hasVoidHeart()
                ? voidHeartAnimation
                : animation;

        TextureRegion frame =
            activeAnimation.getKeyFrame(stateTime);

        if (direction == -1) {
            batch.draw(
                frame,
                bounds.x + bounds.width,
                bounds.y,
                -bounds.width,
                bounds.height
            );
        } else {
            batch.draw(
                frame,
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height
            );
        }
    }

    public void dispose() {
    }

    public static void disposeShared() {
        if (sharedAnimation != null) {
            sharedAnimation.getKeyFrames()[0].getTexture().dispose();
            sharedVoidHeartAnimation.getKeyFrames()[0].getTexture().dispose();
            sharedAnimation = null;
            sharedVoidHeartAnimation = null;
        }
    }

}
