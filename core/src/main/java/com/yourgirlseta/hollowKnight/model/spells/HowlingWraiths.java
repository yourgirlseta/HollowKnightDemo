package com.yourgirlseta.hollowKnight.model.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.yourgirlseta.hollowKnight.model.charms.CharmManager;
import com.yourgirlseta.hollowKnight.model.enemy.Enemy;

public class HowlingWraiths {

    private Rectangle bounds;

    private Animation<TextureRegion> animation;
    private Animation<TextureRegion> voidHeartAnimation;
    private static Animation<TextureRegion> sharedAnimation;
    private static Animation<TextureRegion> sharedVoidHeartAnimation;

    private float stateTime = 0f;
    private float lifetime = 0.45f;
    private float damageTimer = 0f;
    private int damageTicks = 0;

    private static final float DAMAGE_INTERVAL = 0.12f;
    private static final int BASE_DAMAGE = 1;

    private final CharmManager charmManager;

    private ObjectMap<Enemy, Integer> enemyHits = new ObjectMap<>();

    public HowlingWraiths(float x, float y, CharmManager charmManager) {
        this.charmManager = charmManager;

        bounds = new Rectangle(x - 80, y, 160, 220);

        loadAnimation();
    }

    private void loadAnimation() {
        if (sharedAnimation == null) {
            Texture sheet = new Texture("animation/Effects/SoulScream.png");
            sharedAnimation = buildAnimation(sheet, 332, 306, 13, 0.05f);

            Texture voidHeartSheet = new Texture("animation/Effects/ShadowScream.png");
            sharedVoidHeartAnimation = buildAnimation(voidHeartSheet, 357, 292, 13, 0.05f);
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
        anim.setPlayMode(Animation.PlayMode.NORMAL);
        return anim;
    }

    public void update(float delta, Array<Enemy> enemies) {
        stateTime += delta;
        lifetime -= delta;
        damageTimer += delta;

        if (damageTimer >= DAMAGE_INTERVAL && damageTicks < 3) {
            damageTimer = 0f;
            damageTicks++;

            int damage = Math.round(BASE_DAMAGE * charmManager.getSpellDamageMultiplier());

            for (Enemy enemy : enemies) {
                if (enemy.isDead()) continue;

                if (bounds.overlaps(enemy.getBounds())) {
                    enemy.takeDamage(damage);
                }
            }
        }
    }

    public boolean isFinished() {
        return lifetime <= 0f;
    }

    public void render(SpriteBatch batch) {
        Animation<TextureRegion> activeAnimation =
            charmManager.hasVoidHeart() ? voidHeartAnimation : animation;

        TextureRegion frame = activeAnimation.getKeyFrame(stateTime);

        batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
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
