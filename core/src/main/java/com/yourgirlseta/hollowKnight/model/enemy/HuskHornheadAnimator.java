package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HuskHornheadAnimator {
    private Animation<TextureRegion> attackAnticipateAnimation;
    private Animation<TextureRegion> attackLungeAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> turnAnimation;

    public HuskHornheadAnimator() {
        attackLungeAnimation = loadAnimation(
            "animation/Husk_Hornhead/AttackLunge.png",
            239,
            219,
            13,
            0.18f,
            Animation.PlayMode.NORMAL
        );

        attackAnticipateAnimation = loadAnimation(
            "animation/Husk_Hornhead/AttackAnticipate.png",
            239,
            219,
            5,
            0.09f,
            Animation.PlayMode.NORMAL
        );

        idleAnimation = loadAnimation(
            "animation/Husk_Hornhead/Idle.png",
            239,
            219,
            6,
            0.09f,
            Animation.PlayMode.NORMAL
        );

        walkingAnimation = loadAnimation(
            "animation/Husk_Hornhead/Walk.png",
            239,
            219,
            7,
            0.09f,
            Animation.PlayMode.LOOP
        );

        deathAirAnimation = loadAnimation(
            "animation/Husk_Hornhead/Deathair.png",
            239,
            219,
            1,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        deathLandAnimation = loadAnimation(
            "animation/Husk_Hornhead/Death Land.png",
            239,
            219,
            8,
            0.03f,
            Animation.PlayMode.NORMAL
        );

        turnAnimation = loadAnimation(
            "animation/Husk_Hornhead/Turn.png",
            239,
            219,
            2,
            0.05f,
            Animation.PlayMode.NORMAL
        );
    }

    enum HuskState{
        ATTACK_ANTICIPATE,
        ATTACK_LUNGE,
        DEATH_AIR,
        DEATH_LAND,
        IDLE,
        TURN,
        WALKING

    }

    public boolean isTurnFinished(float stateTime) {
        return turnAnimation.isAnimationFinished(stateTime);
    }

    public boolean isAttackAnticipateFinished(float stateTime) {
        return attackAnticipateAnimation.isAnimationFinished(stateTime);
    }

    public boolean isAttackLungeFinished(float stateTime) {
        return attackLungeAnimation.isAnimationFinished(stateTime);
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
        HuskHornheadAnimator.HuskState state,
        float stateTime
    ) {

        switch (state) {

            case WALKING:

                return walkingAnimation.getKeyFrame(
                    stateTime,
                    true
                );

            case IDLE:

                return idleAnimation.getKeyFrame(
                    stateTime,
                    true
                );

            case ATTACK_ANTICIPATE:

                return attackAnticipateAnimation.getKeyFrame(
                    stateTime
                );

            case ATTACK_LUNGE:

                return attackLungeAnimation.getKeyFrame(
                    stateTime
                );

            case DEATH_AIR:

                return deathAirAnimation.getKeyFrame(
                    stateTime
                );

            case DEATH_LAND:

                return deathLandAnimation.getKeyFrame(
                    deathLandAnimation
                        .getAnimationDuration()
                );

            case TURN:

                return turnAnimation.getKeyFrame(
                    stateTime
                );
        }

        return idleAnimation.getKeyFrame(
            stateTime,
            true
        );
    }
}
