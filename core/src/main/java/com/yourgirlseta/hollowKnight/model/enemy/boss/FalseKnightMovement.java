package com.yourgirlseta.hollowKnight.model.enemy.boss;

import com.badlogic.gdx.math.Rectangle;

public class FalseKnightMovement {

    private float moveSpeed = 150f;
    private float chargeSpeed = 650f;
    private float jumpSpeed = 600f;
    private float gravity = 1300f;

    private float velocityX;
    private float velocityY;

    private boolean grounded = true;

    public void moveTowards(
        Rectangle bounds,
        float targetX,
        float delta
    ) {

        if (targetX > bounds.x) {
            velocityX = moveSpeed;
        } else {
            velocityX = -moveSpeed;
        }

        bounds.x += velocityX * delta;
    }

    public void chargeTowards(
        Rectangle bounds,
        float targetX,
        float delta
    ) {

        if (targetX > bounds.x) {
            velocityX = chargeSpeed;
        } else {
            velocityX = -chargeSpeed;
        }

        bounds.x += velocityX * delta;
    }

    public void jump(
        boolean faceRight
    ) {

        if (!grounded)
            return;

        grounded = false;

        velocityY = jumpSpeed;

        velocityX = faceRight
            ? moveSpeed
            : -moveSpeed;
    }

    public void defensiveJump(
        boolean faceRight
    ) {

        if (!grounded)
            return;

        grounded = false;

        velocityY = jumpSpeed;

        velocityX = faceRight
            ? -moveSpeed
            : moveSpeed;
    }

    public void powerJump(
        boolean faceRight
    ) {

        if (!grounded)
            return;

        grounded = false;

        velocityY = jumpSpeed * 1.3f;

        velocityX = faceRight
            ? moveSpeed * 0.5f
            : -moveSpeed * 0.5f;
    }

    public void update(
        Rectangle bounds,
        float groundY,
        float delta
    ) {

        if (!grounded) {

            velocityY -= gravity * delta;

            bounds.x += velocityX * delta;
            bounds.y += velocityY * delta;

            if (bounds.y <= groundY) {

                bounds.y = groundY;

                velocityY = 0f;
                velocityX = 0f;

                grounded = true;
            }
        }
    }

    public void powerJumpToward(Rectangle bounds, float targetCenterX) {
        if (!grounded) return;
        grounded = false;

        velocityY = jumpSpeed * 1.3f;

        float airTime    = 2f * velocityY / gravity;
        float bossCenter = bounds.x + bounds.width / 2f;
        float dx         = targetCenterX - bossCenter;

        velocityX = dx / airTime;
        velocityX = Math.max(-chargeSpeed, Math.min(chargeSpeed, velocityX));
    }

    public void stop() {

        velocityX = 0f;
    }

    public boolean isGrounded() {

        return grounded;
    }

    public float getVelocityX() {

        return velocityX;
    }

    public float getVelocityY() {

        return velocityY;
    }

    public void setMoveSpeed(float moveSpeed) {

        this.moveSpeed = moveSpeed;
    }

    public void setChargeSpeed(float chargeSpeed) {

        this.chargeSpeed = chargeSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {

        this.jumpSpeed = jumpSpeed;
    }

    public void charge(Rectangle bounds, int direction, float delta) {

        bounds.x += chargeSpeed * direction * delta;
    }
}
