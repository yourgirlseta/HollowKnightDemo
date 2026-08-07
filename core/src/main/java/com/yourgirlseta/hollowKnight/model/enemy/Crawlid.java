package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public class Crawlid extends GroundEnemy {

    private boolean isTurning = false;

    private float hitCooldown = 0f;

    private static final float WIDTH = 220f;
    private static final float HEIGHT = 120f;

    private static final float HITBOX_WIDTH = 100f;
    private static final float HITBOX_HEIGHT = 70f;

    private static final float GRAVITY = -1800f;

    private final CrawlidAnimator animator;

    private float stateTime = 0f;
    private float velocityY = 0f;
    private float velocityX = 0f;

    private CrawlidAnimator.CrawlidState currentState =
        CrawlidAnimator.CrawlidState.WALKING;

    public Crawlid(
        float x,
        float y,
        float speed,
        int hp,
        int direction
    ) {

        this.bounds = new Rectangle(
            x - HITBOX_WIDTH / 2f,
            y - HITBOX_HEIGHT,
            HITBOX_WIDTH,
            HITBOX_HEIGHT
        );

        this.speed = speed;
        this.hp = hp;
        this.direction = direction;

        animator = new CrawlidAnimator();
    }

    @Override
    public Rectangle getBounds() {

        return bounds;
    }

    @Override
    public void stopMovement() {

        velocityX = 0f;
    }


    @Override
    public void update(
        float delta,
        FirstMap map,
        Player player
    ) {

        if (hitCooldown > 0f) {
            hitCooldown -= delta;
        }

        if (isTurning) {
            stateTime += delta;

            if (animator.isTurnFinished(stateTime)) {
                isTurning = false;

                currentState = CrawlidAnimator.CrawlidState.WALKING;

                stateTime = 0f;
            }
            return;
        }

        stateTime += delta;

        if (dead) {
            if (currentState == CrawlidAnimator.CrawlidState.DEAD_AIR) {
                velocityY += GRAVITY * delta;
                bounds.x += knockbackVelocityX * delta;
                knockbackVelocityX *= 0.96f;
                bounds.y += velocityY * delta;

                for (Rectangle tile :
                    map.getGroundTiles()) {

                    if (
                        bounds.overlaps(tile)
                            &&
                            velocityY <= 0f
                    ) {

                        bounds.y =
                            tile.y + tile.height;

                        velocityY = 0f;

                        currentState =
                            CrawlidAnimator.CrawlidState.DEAD_LAND;

                        stateTime = 0f;

                        break;
                    }
                }
            }

            return;
        }

        velocityY += GRAVITY * delta;
        bounds.y += velocityY * delta;

        boolean grounded = false;

        Rectangle feet = new Rectangle(
            bounds.x + 20f,
            bounds.y,
            bounds.width - 40f,
            8f
        );

        for (Rectangle tile :
            map.getGroundTiles()) {

            if (
                feet.overlaps(tile)
                    &&
                    velocityY <= 0f
            ) {

                bounds.y =
                    tile.y + tile.height;

                velocityY = 0f;

                grounded = true;

                break;
            }
        }

        if (isKnocked) {

            bounds.x +=
                knockbackVelocityX * delta;

            knockbackVelocityX *= 0.85f;

            if (
                Math.abs(
                    knockbackVelocityX
                ) < 20f
            ) {

                knockbackVelocityX = 0f;

                isKnocked = false;
            }

            return;
        }

        currentState =
            CrawlidAnimator.CrawlidState.WALKING;

        float oldX = bounds.x;

        bounds.x +=
            speed *
                direction *
                delta;

        if (
            isCollidingWithWall(
                map.getEnemyCollisionTiles()
            )
        ) {

            bounds.x = oldX;

            direction *= -1;

            isTurning = true;

            currentState =
                CrawlidAnimator.CrawlidState.TURNING;

            stateTime = 0f;
        }

        if (
            grounded
                &&
                !hasGroundAhead(
                    map.getEnemyCollisionTiles()
                )
        ) {

            bounds.x = oldX;

            direction *= -1;

            isTurning = true;

            currentState =
                CrawlidAnimator.CrawlidState.TURNING;

            stateTime = 0f;
        }
    }

    public void takeDamage(
        int damage
    ) {

        if (dead)
            return;

        if (hitCooldown > 0f)
            return;

        hitCooldown = 0.25f;

        hp -= damage;

        if (hp <= 0) {

            die();
        }
    }

    private void die() {

        dead = true;
        notifyDeath();

        velocityY = 450f;

        knockbackVelocityX =
            direction == 1
                ? -180f
                : 180f;

        currentState = CrawlidAnimator.CrawlidState.DEAD_AIR;

        stateTime = 0f;
    }

    private boolean isCollidingWithWall(
        Array<Rectangle> grounds
    ) {

        for (Rectangle tile : grounds) {

            if (bounds.overlaps(tile)) {

                return true;
            }
        }

        return false;
    }

    private boolean hasGroundAhead(
        Array<Rectangle> grounds
    ) {

        float checkX =
            direction == 1
                ? bounds.x +
                  bounds.width +
                  10f
                : bounds.x - 10f;

        float checkY =
            bounds.y - 10f;

        Rectangle probe =
            new Rectangle(
                checkX,
                checkY,
                8f,
                8f
            );

        for (Rectangle tile : grounds) {

            if (probe.overlaps(tile)) {

                return true;
            }
        }

        return false;
    }

    @Override
    public void render(
        SpriteBatch batch
    ) {

        TextureRegion frame =
            animator.getFrame(
                currentState,
                stateTime
            );

        batch.draw(
            frame,

            direction == -1
                ? bounds.x - 55f
                : bounds.x + WIDTH - 55f,

            bounds.y - 20f,

            direction == -1
                ? WIDTH
                : -WIDTH,

            HEIGHT
        );
    }
}
