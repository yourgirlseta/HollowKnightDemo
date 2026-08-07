package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CrawlidAnimator {

    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> deathAirAnimation;
    private Animation<TextureRegion> deathLandAnimation;
    private Animation<TextureRegion> turnAnimation;

    public CrawlidAnimator() {
        walkingAnimation = loadAnimation(
            "animation/Crawlid/walikingCrawlid.png",
            301,
            149,
            4,
            0.09f,
            Animation.PlayMode.LOOP
        );

        deathAirAnimation = loadAnimation(
            "animation/Crawlid/deathAirAnimation.png",
            303,
            177,
            3,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        deathLandAnimation = loadAnimation(
            "animation/Crawlid/deathLandAnimation.png",
            303,
            177,
            2,
            0.03f,
            Animation.PlayMode.NORMAL
        );

        turnAnimation = loadAnimation(
            "animation/Crawlid/turnAnimation.png",
            301,
            149,
            2,
            0.03f,
            Animation.PlayMode.NORMAL
        );
    }

    enum CrawlidState {
        WALKING,
        TURNING,
        DEAD_AIR,
        DEAD_LAND
    }

    public boolean isTurnFinished(
        float stateTime
    ) {

        return turnAnimation.isAnimationFinished(
            stateTime
        );
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
        CrawlidState state,
        float stateTime
    ) {

        switch (state) {

            case WALKING:

                return walkingAnimation.getKeyFrame(
                    stateTime,
                    true
                );

            case TURNING:

                return turnAnimation.getKeyFrame(
                    stateTime
                );

            case DEAD_AIR:

                return deathAirAnimation.getKeyFrame(
                    stateTime
                );

            case DEAD_LAND:

                return deathLandAnimation.getKeyFrame(
                    deathLandAnimation
                        .getAnimationDuration()
                );
        }

        return walkingAnimation.getKeyFrame(
            stateTime,
            true
        );
    }
}
