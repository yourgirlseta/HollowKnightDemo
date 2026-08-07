package com.yourgirlseta.hollowKnight.model.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourgirlseta.hollowKnight.model.CheatManager;

public class HealthHud {
    private final CheatManager cheatManager;
    private boolean invincible = false;
    private float invincibleTimer = 0f;
    private static final float INVINCIBLE_DURATION = 1f;
    private static final float BLINK_INTERVAL = 0.1f;

    private static final int MAX_MASKS = 5;
    private int currentHealth = 5;

    private final MaskState[] maskStates = new MaskState[MAX_MASKS];
    private final float[] maskStateTimes = new float[MAX_MASKS];

    private Animation<TextureRegion> fullAnimation;
    private Animation<TextureRegion> breakAnimation;
    private TextureRegion maskEmpty;
    private Animation<TextureRegion> healAnimation;

    private boolean emergencyHealUsed = false;

    public enum MaskState {
        FULL,
        BREAKING,
        EMPTY,
        HEALING
    }

    public HealthHud(CheatManager cheatManager) {
        this.cheatManager = cheatManager;

        for (int i = 0; i < MAX_MASKS; i++) {
            maskStates[i] = MaskState.FULL;
            maskStateTimes[i] = 0f;
        }

        fullAnimation = loadAnimation(
            "animation/HUD/FilledHealthShine (2).png",
            126,
            167,
            5,
            0.005f,
            Animation.PlayMode.LOOP);

        breakAnimation = loadAnimation(
            "animation/HUD/BreakHealth.png",
            126,
            167,
            6,
            0.08f,
            Animation.PlayMode.NORMAL);

        Texture emptyTex = new Texture("animation/HUD/EmptyHealth.png");

        maskEmpty = new TextureRegion(emptyTex);

        healAnimation = loadAnimation(
            "animation/HUD/HealthRefill.png",
            126,
            167,
            5,
            0.08f,
            Animation.PlayMode.NORMAL);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public boolean isFullHealth() {

        return currentHealth == MAX_MASKS;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public boolean takeDamage() {

        if (cheatManager.isGodMode()) {
            return false;
        }

        if (cheatManager.isNoclip()) {
            return false;
        }

        if (invincible || currentHealth <= 0) {
            return false;
        }
        currentHealth--;

        maskStates[currentHealth] =
            MaskState.BREAKING;

        maskStateTimes[currentHealth] =
            0f;

        if (
            currentHealth <= 0
            &&
        cheatManager.isEmergencyHeal()
            &&
            !emergencyHealUsed
        ) {

            emergencyHealUsed = true;

            currentHealth = 1;

            maskStates[0] =
                MaskState.HEALING;

            maskStateTimes[0] =
                0f;
        }

        invincible = true;

        invincibleTimer = 0f;

        return true;
    }

    public boolean isNoclip() {

        return cheatManager.isNoclip();
    }

    public void heal() {
        if (currentHealth < MAX_MASKS) {
            maskStates[currentHealth] = MaskState.HEALING;
            maskStateTimes[currentHealth] = 0f;
            currentHealth++;
        }
    }

    public void update(float delta) {
        for (int i = 0; i < MAX_MASKS; i++) {
            maskStateTimes[i] += delta;

            if (maskStates[i] == MaskState.BREAKING && breakAnimation.isAnimationFinished(maskStateTimes[i])) {
                maskStates[i] = MaskState.EMPTY;
            }

            if (maskStates[i] == MaskState.HEALING && healAnimation.isAnimationFinished(maskStateTimes[i])) {
                maskStates[i] = MaskState.FULL;
            }
        }

        if (invincible) {
            invincibleTimer += delta;
            if (invincibleTimer >= INVINCIBLE_DURATION) {
                invincible = false;
                invincibleTimer = 0f;
            }
        }

    }

    public void resetHealth() {

        currentHealth = MAX_MASKS;

        for (int i = 0; i < MAX_MASKS; i++) {

            maskStates[i] = MaskState.FULL;
            maskStateTimes[i] = 0f;
        }
        emergencyHealUsed = false;
        invincible = false;
        invincibleTimer = 0f;
    }

    public void render(SpriteBatch batch) {
        boolean visible = !invincible || ((int)(invincibleTimer / BLINK_INTERVAL) % 2 == 0);
        if (!visible) {
            return;
        }

        float startX = 160;
        float startY = 1000;
        float spacing = 70;

        int missingMasks = MAX_MASKS - currentHealth;

        float darkStep = 0.15f;
        float minBrightness = 0.4f;
        float brightness = 1f - (missingMasks * darkStep);
        brightness = Math.max(minBrightness, brightness);

        for (int i = 0; i < MAX_MASKS; i++) {
            TextureRegion currentFrame = null;
            boolean isEmpty = false;

            switch (maskStates[i]) {
                case FULL:
                    currentFrame = fullAnimation.getKeyFrame(maskStateTimes[i]);
                    break;
                case BREAKING:
                    currentFrame = breakAnimation.getKeyFrame(maskStateTimes[i]);
                    break;
                case EMPTY:
                    currentFrame = maskEmpty;
                    isEmpty = true;
                    break;
                case HEALING:
                    currentFrame = healAnimation.getKeyFrame(maskStateTimes[i]);
                    break;
            }

            if (currentFrame != null) {

                if (isEmpty) {
                    batch.setColor(0.35f, 0.35f, 0.35f, 1f);
                } else {
                    batch.setColor(brightness, brightness, brightness, 1f);
                }

                batch.draw(currentFrame, startX + (i * spacing), startY, 64, 64);

                batch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    private Animation<TextureRegion> loadAnimation(String path, int width, int height, int count, float duration, Animation.PlayMode mode) {
        Texture sheet = new Texture(path);
        TextureRegion[][] temp = TextureRegion.split(sheet, width, height);

        int availableFrames = temp.length * (temp.length > 0 ? temp[0].length : 0);
        if (availableFrames < count) {
            System.err.println("WARNING: File " + path + " only has " + availableFrames +
                " frames, but you requested " + count + "!");
            count = availableFrames;
        }

        TextureRegion[] frames = new TextureRegion[count];
        int index = 0;
        for (int r = 0; r < temp.length; r++) {
            for (int c = 0; c < temp[r].length; c++) {
                if (index < count) {
                    frames[index++] = temp[r][c];
                }
            }
        }
        Animation<TextureRegion> anim = new Animation<>(duration, frames);
        anim.setPlayMode(mode);
        return anim;
    }

    public void dispose() {
        fullAnimation.getKeyFrames()[0].getTexture().dispose();
        breakAnimation.getKeyFrames()[0].getTexture().dispose();
        healAnimation.getKeyFrames()[0].getTexture().dispose();
        maskEmpty.getTexture().dispose();
    }

}
