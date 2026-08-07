package com.yourgirlseta.hollowKnight.model.enemy.boss;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourgirlseta.hollowKnight.model.enemy.CrawlidAnimator;
import com.yourgirlseta.hollowKnight.model.enums.FalseKnightState;

public class FalseKnightAnimator {
    private Animation<TextureRegion> attackAnticAnimation;
    private Animation<TextureRegion> attackRecoverAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> bodyStunAnimation;
    private Animation<TextureRegion> deathFallAnimation;
    private Animation<TextureRegion> deathHitAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> jumpAttackAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> landAnimation;
    private Animation<TextureRegion> runAnticAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> stunRecoverAnimation;
    private Animation<TextureRegion> turnAnimation;

    public FalseKnightAnimator() {
        attackAnticAnimation = loadAnimation(
            "animation/False_knight/Attack Antic.png",
            1095,
            636,
            6,
            0.12f,
            Animation.PlayMode.NORMAL
        );
        attackRecoverAnimation = loadAnimation(
            "animation/False_knight/Attack Recover.png",
            1095,
            636,
            5,
            0.12f,
            Animation.PlayMode.NORMAL
            );
        attackAnimation = loadAnimation(
            "animation/False_knight/Attack.png",
            1095,
            636,
            3,
            0.05f,
            Animation.PlayMode.NORMAL
        );
        bodyStunAnimation = loadAnimation(
            "animation/False_knight/Body.png",
            1095,
            636,
            5,
            0.1f,
            Animation.PlayMode.NORMAL
        );
        deathFallAnimation = loadAnimation(
            "animation/False_knight/DeathFall.png",
            1095,
            636,
            3,
            0.05f,
            Animation.PlayMode.NORMAL
        );
        deathHitAnimation = loadAnimation(
            "animation/False_knight/DeathHit.png",
            1095,
            636,
            3,
            0.05f,
            Animation.PlayMode.NORMAL
        );
        deathLandAnimation = loadAnimation(
            "animation/False_knight/DeathLand.png",
            1095,
            636,
            11,
            0.2f,
            Animation.PlayMode.NORMAL
        );
        idleAnimation = loadAnimation(
            "animation/False_knight/Idle.png",
            1095,
            636,
            5,
            0.1f,
            Animation.PlayMode.LOOP
        );
        jumpAttackAnimation = loadAnimation(
            "animation/False_knight/Jump Attack.png",
            1095,
            636,
            8,
            0.15f,
            Animation.PlayMode.NORMAL
        );
        jumpAnimation = loadAnimation(
            "animation/False_knight/Jump.png",
            1095,
            636,
            4,
            0.05f,
            Animation.PlayMode.NORMAL
        );
        landAnimation = loadAnimation("animation/False_knight/Land.png",
            1095,
            636,
            5,
            0.05f,
            Animation.PlayMode.NORMAL
            );
        runAnticAnimation = loadAnimation(
            "animation/False_knight/Run Antic.png",
            1095,
            636,
            2,
            0.02f,
            Animation.PlayMode.NORMAL
        );
        runAnimation = loadAnimation(
            "animation/False_knight/Run.png",
            1095,
            636,
            5,
            0.12f,
            Animation.PlayMode.LOOP
        );
        stunRecoverAnimation = loadAnimation(
            "animation/False_knight/Stun Recover.png",
            1095,
            636,
            6,
            0.12f,
            Animation.PlayMode.NORMAL
        );
        turnAnimation = loadAnimation(
            "animation/False_knight/Turn.png",
            1095,
            636,
            2,
            0.1095f,
            Animation.PlayMode.NORMAL
        );
    }

    public boolean isFinished(FalseKnightState state, float stateTime) {

        switch (state) {

            case ATTACK_ANTIC:
                return attackAnticAnimation.isAnimationFinished(stateTime);

            case ATTACK:
                return attackAnimation.isAnimationFinished(stateTime);

            case ATTACK_RECOVER:
                return attackRecoverAnimation.isAnimationFinished(stateTime);

            case STUN:
                return bodyStunAnimation.isAnimationFinished(stateTime);

            case STUN_RECOVER:
                return stunRecoverAnimation.isAnimationFinished(stateTime);

            case DEATH_FALL:
                return deathFallAnimation.isAnimationFinished(stateTime);

            case DEATH_HIT:
                return deathHitAnimation.isAnimationFinished(stateTime);

            case DEATH_LAND:
                return deathLandAnimation.isAnimationFinished(stateTime);

            case JUMP:
                return jumpAnimation.isAnimationFinished(stateTime);

            case JUMP_ATTACK:
                return jumpAttackAnimation.isAnimationFinished(stateTime);

            case LAND:
                return landAnimation.isAnimationFinished(stateTime);

            case RUN_ANTIC:
                return runAnticAnimation.isAnimationFinished(stateTime);

            case TURN:
                return turnAnimation.isAnimationFinished(stateTime);

            default:
                return false;
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

    public TextureRegion getFrame(
        FalseKnightState state,
        float stateTime
    ) {

        switch (state) {

            case ATTACK_ANTIC:
                return attackAnticAnimation.getKeyFrame(stateTime);

            case ATTACK_RECOVER:
                return attackRecoverAnimation.getKeyFrame(stateTime);

            case ATTACK:
                return attackAnimation.getKeyFrame(stateTime);

            case STUN:
                return bodyStunAnimation.getKeyFrame(stateTime);

            case STUN_RECOVER:
                return stunRecoverAnimation.getKeyFrame(stateTime);

            case DEATH_FALL:
                return deathFallAnimation.getKeyFrame(stateTime);

            case DEATH_HIT:
                return deathHitAnimation.getKeyFrame(stateTime);

            case DEATH_LAND:
                return deathLandAnimation.getKeyFrame(stateTime);

            case IDLE:
                return idleAnimation.getKeyFrame(stateTime);

            case JUMP_ATTACK:
                return jumpAttackAnimation.getKeyFrame(stateTime);

            case JUMP:
                return jumpAnimation.getKeyFrame(stateTime);

            case LAND:
                return landAnimation.getKeyFrame(stateTime);

            case RUN_ANTIC:
                return runAnticAnimation.getKeyFrame(stateTime);

            case RUN:
                return runAnimation.getKeyFrame(stateTime);

            case TURN:
                return turnAnimation.getKeyFrame(stateTime);

            default:
                return idleAnimation.getKeyFrame(stateTime);
        }
    }
}
