package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class LaserAnimator {

    private static final String
        GUARDIAN_LASER_PATH =
        "animation/Crystallized/atlas0 #25304.png";

    private Texture
        guardianLaserTexture;

    private Animation<TextureRegion>
        guardianLaserAnimation;

    private float
        laserStateTime = 0f;

    private static final int
        GUARDIAN_LASER_FRAMES = 26;

    private static final float
        GUARDIAN_LASER_FRAME_DURATION =
        0.03f;

    public LaserAnimator () {
        guardianLaserAnimation =
            loadGuardianLaserAnimation();
    }

    private Animation<TextureRegion>
    loadGuardianLaserAnimation() {

        if (
            Gdx.files
                .internal(
                    GUARDIAN_LASER_PATH
                )
                .exists()
        ) {

            guardianLaserTexture =
                new Texture(
                    Gdx.files.internal(
                        GUARDIAN_LASER_PATH
                    )
                );

            guardianLaserTexture.setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
            );

        } else {

            guardianLaserTexture =
                createGuardianLaserFallbackTexture();
        }

        int frameWidth =
            guardianLaserTexture.getWidth()
                / GUARDIAN_LASER_FRAMES;

        int frameHeight =
            guardianLaserTexture.getHeight();

        TextureRegion[][] splitFrames =
            TextureRegion.split(
                guardianLaserTexture,
                frameWidth,
                frameHeight
            );

        TextureRegion[] frames =
            new TextureRegion[
                GUARDIAN_LASER_FRAMES
                ];

        for (
            int i = 0;
            i < GUARDIAN_LASER_FRAMES;
            i++
        ) {

            frames[i] =
                splitFrames[0][i];
        }

        Animation<TextureRegion>
            animation =
            new Animation<>(
                GUARDIAN_LASER_FRAME_DURATION,
                frames
            );

        animation.setPlayMode(
            Animation.PlayMode.LOOP
        );

        return animation;
    }

    public TextureRegion getFrame(
        float stateTime
    ) {

        return guardianLaserAnimation
            .getKeyFrame(
                stateTime,
                true
            );
    }

    private Texture
    createGuardianLaserFallbackTexture() {

        Pixmap pixmap =
            new Pixmap(
                64,
                16,
                Pixmap.Format.RGBA8888
            );

        pixmap.setColor(
            Color.CYAN
        );

        pixmap.fill();

        Texture texture =
            new Texture(
                pixmap
            );

        pixmap.dispose();

        return texture;
    }
}
