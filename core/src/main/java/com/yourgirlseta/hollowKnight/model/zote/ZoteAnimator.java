package com.yourgirlseta.hollowKnight.model.zote;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourgirlseta.hollowKnight.model.enemy.CrawlidAnimator;
import com.yourgirlseta.hollowKnight.model.enums.ZoteState;

public class ZoteAnimator {

    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> fallAnimation;
    private Animation<TextureRegion> getUpAnimation;;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> rollAnimation;
    private Animation<TextureRegion> talkAnimation;
    private Animation<TextureRegion> turnAnimation;

    public ZoteAnimator() {
        attackAnimation = loadAnimation(
            "animation/Zote/spritesheet.png",
            349,
            186,
            4,
            0.12f,
            Animation.PlayMode.NORMAL
        );

        fallAnimation = loadAnimation(
            "animation/Zote/spritesheet (1).png",
            349,
            186,
            5,
            0.14f,
            Animation.PlayMode.NORMAL
        );

        getUpAnimation = loadAnimation(
            "animation/Zote/spritesheet (2).png",
            349,
            186,
            4,
            0.12f,
            Animation.PlayMode.NORMAL
        );

        idleAnimation = loadAnimation(
            "animation/Zote/spritesheet (4).png",
            349,
            186,
            5,
            0.14f,
            Animation.PlayMode.LOOP
        );

        rollAnimation = loadAnimation(
            "animation/Zote/spritesheet (3).png",
            349,
            186,
            3,
            0.1f,
            Animation.PlayMode.NORMAL
        );

        talkAnimation = loadAnimation(
            "animation/Zote/spritesheet (5).png",
            349,
            186,
            5,
            0.14f,
            Animation.PlayMode.LOOP
        );

        turnAnimation = loadAnimation(
            "animation/Zote/spritesheet (6).png",
            349,
            186,
            2,
            0.07f,
            Animation.PlayMode.NORMAL
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
        ZoteState state,
        float stateTime
    ) {

        switch (state) {

            case ATTACK:
                return attackAnimation.getKeyFrame(stateTime);

            case FALL:
                return fallAnimation.getKeyFrame(stateTime);

            case GET_UP:
                return getUpAnimation.getKeyFrame(stateTime);

            case ROLL:
                return rollAnimation.getKeyFrame(stateTime);

            case TALKING:
                return talkAnimation.getKeyFrame(stateTime);

            case TURN:
                return turnAnimation.getKeyFrame(stateTime);

            case IDLE:
            default:
                return idleAnimation.getKeyFrame(stateTime);
        }
    }

    public boolean isFinished(
        ZoteState state,
        float stateTime
    ) {

        switch (state) {

            case ATTACK:
                return attackAnimation.isAnimationFinished(stateTime);

            case FALL:
                return fallAnimation.isAnimationFinished(stateTime);

            case GET_UP:
                return getUpAnimation.isAnimationFinished(stateTime);

            case ROLL:
                return rollAnimation.isAnimationFinished(stateTime);

            case TURN:
                return turnAnimation.isAnimationFinished(stateTime);

            default:
                return false;
        }
    }
}
