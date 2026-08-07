package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CrystalGuardianAnimator {

    private Animation<TextureRegion> evadeAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> shootAnimation;
    private Animation<TextureRegion> turnAnimation;

    public CrystalGuardianAnimator() {
        deathAirAnimation = loadAnimation(
            "animation/Crystallized/Death Air.png",
            285,
            189,
            3,
            0.03f,
            Animation.PlayMode.NORMAL
        );

        deathLandAnimation = loadAnimation(
            "animation/Crystallized/Death Land.png",
            285,
            189,
            3,
            0.03f,
            Animation.PlayMode.NORMAL
        );

        evadeAnimation = loadAnimation(
            "animation/Crystallized/Evade.png",
            285,
            189,
            7,
            0.06f,
            Animation.PlayMode.NORMAL
        );

        idleAnimation = loadAnimation(
            "animation/Crystallized/Idle.png",
            285,
            189,
            5,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        runAnimation = loadAnimation(
            "animation/Crystallized/Run.png",
            285,
            189,
            6,
            0.05f,
            Animation.PlayMode.LOOP
        );

        shootAnimation = loadAnimation(
            "animation/Crystallized/Shoot.png",
            285,
            189,
            7,
            0.12f,
            Animation.PlayMode.NORMAL
        );

        turnAnimation = loadAnimation(
            "animation/Crystallized/Turn.png",
            285,
            189,
            3,
            0.03f,
            Animation.PlayMode.NORMAL
        );

    }

    public boolean isShootFinished(
        float stateTime
    ) {

        return shootAnimation
            .isAnimationFinished(
                stateTime
            );
    }

    public boolean isEvadeFinished(
        float stateTime
    ) {

        return evadeAnimation
            .isAnimationFinished(
                stateTime
            );
    }

    public boolean isTurnFinished(
        float stateTime
    ) {

        return turnAnimation
            .isAnimationFinished(
                stateTime
            );
    }

    public boolean isDeathLandFinished(
        float stateTime
    ) {

        return deathLandAnimation
            .isAnimationFinished(
                stateTime
            );
    }

    public enum GuardianState {

        IDLE,
        SHOOT,
        RUN,
        EVADE,
        TURN,
        DEATH_AIR,
        DEATH_LAND,
        RETURN
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
        GuardianState state,
        float stateTime
    ) {

        switch (state) {

            case IDLE:

                return idleAnimation
                    .getKeyFrame(
                        stateTime,
                        true
                    );

            case SHOOT:

                return shootAnimation
                    .getKeyFrame(
                        stateTime
                    );

            case RUN:

                return runAnimation
                    .getKeyFrame(
                        stateTime,
                        true
                    );

            case EVADE:

                return evadeAnimation
                    .getKeyFrame(
                        stateTime
                    );

            case TURN:

                return turnAnimation
                    .getKeyFrame(
                        stateTime
                    );

            case DEATH_AIR:

                return deathAirAnimation
                    .getKeyFrame(
                        stateTime
                    );

            case DEATH_LAND:

                return deathLandAnimation
                    .getKeyFrame(
                        deathLandAnimation
                            .getAnimationDuration()
                    );
        }

        return idleAnimation
            .getKeyFrame(
                stateTime,
                true
            );
    }
}
