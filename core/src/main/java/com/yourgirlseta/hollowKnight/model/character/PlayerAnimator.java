package com.yourgirlseta.hollowKnight.model.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.yourgirlseta.hollowKnight.model.enums.AttackDirection;
import com.yourgirlseta.hollowKnight.model.enums.PlayerState;

import com.badlogic.gdx.math.Rectangle;

public class PlayerAnimator {

    private PlayerState currentState = PlayerState.IDLE;
    private AttackDirection attackDirection = AttackDirection.FORWARD;
    private float stateTime = 0f;
    private int facingDirection = 1;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> jumpingAnimation;
    private Animation<TextureRegion> fallingAnimation;
    private Animation<TextureRegion> dashingAnimation;
    private Animation<TextureRegion> sharpShadowDashAnimation;
    private Animation<TextureRegion> attackingForwardAnimation;
    private Animation<TextureRegion> attackingDownAnimation;
    private Animation<TextureRegion> attackingUpAnimation;
    private Animation<TextureRegion> doubleJumpingAnimation;
    private Animation<TextureRegion> focusingAnimation;
    private Animation<TextureRegion> wallSlidingAnimation;
    private Animation<TextureRegion> deadAnimation;

    public PlayerAnimator() {
        loadAllAnimations();
    }

    public void update(float stateTime, PlayerState currentState, AttackDirection attackDirection, int facingDirection) {
        this.stateTime = stateTime;
        this.currentState = currentState;
        this.attackDirection = attackDirection;
        this.facingDirection = facingDirection;
    }

    private void loadAllAnimations() {
        idleAnimation = loadAnimation(
            "animation/IdleAnimation.png",
            349,
            186,
            9,
            0.12f,
            Animation.PlayMode.LOOP
        );

        runningAnimation = loadAnimation(
            "animation/RunAnimation.png",
            349,
            189,
            12,
            0.08f,
            Animation.PlayMode.LOOP
        );

        jumpingAnimation = loadAnimation(
            "animation/AirbroneAnimation.png",
            349,
            186,
            12,
            0.08f,
            Animation.PlayMode.NORMAL
        );

        fallingAnimation = loadAnimation(
            "animation/Fall.png",
            349,
            186,
            6,
            0.1f,
            Animation.PlayMode.LOOP
        );

        dashingAnimation = loadAnimation(
            "animation/DashAnimation.png",
            349,
            186,
            12,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        sharpShadowDashAnimation = loadAnimation(
            "Shadow Dash.png",
            349, 186,
            11,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        attackingForwardAnimation = loadAnimation(
            "animation/SlashAlt.png",
            349,
            186,
            5,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        attackingDownAnimation = loadAnimation(
            "animation/DownSlash.png",
            349,
            186,
            5,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        attackingUpAnimation = loadAnimation(
            "animation/UpSlash.png",
            349,
            186,
            5,
            0.05f,
            Animation.PlayMode.NORMAL
        );

        doubleJumpingAnimation = loadAnimation(
            "animation/Double Jump.png",
            349,
            186,
            8,
            0.08f,
            Animation.PlayMode.NORMAL
        );

        focusingAnimation = loadAnimation(
            "animation/FocusSprite.png",
            349,
            186,
            12,
            0.12f,
            Animation.PlayMode.NORMAL
        );

        wallSlidingAnimation = loadAnimation(
            "animation/Wall Slide.png",
            349,
            186,
            4,
            0.08f,
            Animation.PlayMode.NORMAL
        );

        deadAnimation = loadAnimation(
            "animation/Death.png",
            117,
            156,
            16,
            0.15f,
            Animation.PlayMode.NORMAL
        );
    }

    private Animation<TextureRegion> loadAnimation(
        String path,
        int frameWidth,
        int frameHeight,
        int frameCount,
        float frameDuration,
        Animation.PlayMode playMode
    ) {
        Texture sheet = new Texture(path);
        TextureRegion[][] grid = TextureRegion.split(sheet, frameWidth, frameHeight);

        int availableFrames = grid.length * (grid.length > 0 ? grid[0].length : 0);
        if (availableFrames < frameCount) {
            System.err.println("WARNING: File " + path + " only has " + availableFrames +
                " frames, but you requested " + frameCount + "!");
            frameCount = availableFrames;
        }

        TextureRegion[] frames = new TextureRegion[frameCount];
        int index = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (index >= frameCount) {
                    break;
                }
                frames[index] = grid[row][col];
                index++;
            }
        }

        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(playMode);
        return animation;
    }


    public void render(SpriteBatch batch, Rectangle bounds) {
        TextureRegion currentFrame;

        switch (currentState) {
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(stateTime);
                break;
            case RUNNING:
                currentFrame = runningAnimation.getKeyFrame(stateTime);
                break;
            case JUMPING:
                currentFrame = jumpingAnimation.getKeyFrame(stateTime);
                break;
            case FALLING:
                currentFrame = fallingAnimation.getKeyFrame(stateTime);
                break;
            case ATTACKING:
                if (attackDirection == AttackDirection.DOWN) {
                    currentFrame = attackingDownAnimation.getKeyFrame(stateTime);
                } else if (attackDirection == AttackDirection.UP){
                    currentFrame = attackingUpAnimation.getKeyFrame(stateTime);
                } else {
                    currentFrame = attackingForwardAnimation.getKeyFrame(stateTime);
                }
                break;

            case DASHING:
                currentFrame = dashingAnimation.getKeyFrame(stateTime);
                break;
            case DASHING_SHARP_SHADOW:
                currentFrame = sharpShadowDashAnimation.getKeyFrame(stateTime);
                break;
            case DOUBLE_JUMPING:
                currentFrame = doubleJumpingAnimation.getKeyFrame(stateTime);
                break;
            case WALL_SLIDING:
                currentFrame = wallSlidingAnimation.getKeyFrame(stateTime);
                break;
            case FOCUSING:
                currentFrame = focusingAnimation.getKeyFrame(stateTime);
                break;
            case DEATH:
                currentFrame = deadAnimation.getKeyFrame(stateTime);
                break;
            default:
                currentFrame = idleAnimation.getKeyFrame(stateTime);
                break;
        }

        TextureRegion frame = new TextureRegion(currentFrame);

        if (
            facingDirection == 1
                &&
                !frame.isFlipX()
        ) {

            frame.flip(true, false);
        }

        else if (
            facingDirection == -1
                &&
                frame.isFlipX()
        ) {

            frame.flip(true, false);
        }

        float drawWidth = 180f;
        float drawHeight = 96f;

        float drawX = bounds.x - (drawWidth - bounds.width) / 2f;
        float drawY = bounds.y;

        batch.draw(frame, drawX, drawY, drawWidth, drawHeight);

    }
}
