package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MossFlyAnimator {
    private Animation<TextureRegion> shakeAnimation;
    private Animation<TextureRegion> appearAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> flyingAnimation;
    private Animation<TextureRegion> turnAnimation;


    public MossFlyAnimator() {
        shakeAnimation = loadAnimation(
            "animation/Mossfly/shakeSprite.png",
            181,
            141,
            3,
            0.05f,
            Animation.PlayMode.LOOP
        );
        appearAnimation = loadAnimation(
            "animation/Mossfly/appearSprite.png",
            181,
            141,
            6,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        deathAirAnimation = loadAnimation(
            "animation/Mossfly/deathairSprite.png",
            181,
            141,
            4,
            0.05f,
            Animation.PlayMode.NORMAL

        );

        deathLandAnimation = loadAnimation(
            "animation/Mossfly/deathlandSprite (2).png",
            181,
            141,
            4,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        flyingAnimation = loadAnimation(
            "animation/Mossfly/flySprite.png",
            181,
            141,
            4,
            0.05f,
            Animation.PlayMode.LOOP
        );

        turnAnimation = loadAnimation(
            "animation/Mossfly/TurnSprite.png",
            181,
            141,
            3,
            0.05f,
            Animation.PlayMode.NORMAL
        );

    }

    enum MossflyState {
        SHAKE,
        APEAR,
        DEATH_AIR,
        DEATH_LAND,
        FLYING,
        TURN
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
        MossFlyAnimator.MossflyState state,
        float stateTime
    ) {

        switch (state) {

            case SHAKE:

                return shakeAnimation.getKeyFrame(
                    stateTime,
                    true
                );

            case APEAR:

                return appearAnimation.getKeyFrame(
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

            case FLYING:

                return flyingAnimation.getKeyFrame(
                    stateTime,
                    true
                );

            case TURN:

                return turnAnimation.getKeyFrame(
                    stateTime
                );
        }

        return flyingAnimation.getKeyFrame(
            stateTime,
            true
        );
    }
}
