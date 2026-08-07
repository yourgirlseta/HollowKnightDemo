package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public class MossFly
    extends FlyingEnemy {

    private int previousDirection = 1;

    private static final float GRAVITY =
        -1800f;

    private float velocityY = 0f;
    private float velocityX = 0f;

    private static final float WIDTH =
        120f;

    private static final float HEIGHT =
        90f;

    private final MossFlyAnimator animator;

    private MossFlyAnimator.MossflyState currentState =
        MossFlyAnimator.MossflyState.SHAKE;

    private float stateTime = 0f;

    private float idleShakeTimer = 0f;

    public MossFly(
        float x,
        float y,
        int hp
    ) {

        this.bounds = new Rectangle(
            x,
            y,
            WIDTH,
            HEIGHT
        );

        this.hp = hp;

        this.flySpeed = 240f;

        this.detectionRange = 500f;

        animator = new MossFlyAnimator();
    }

    private boolean isCollidingWall(
        Array<Rectangle> grounds
    ) {

        for (Rectangle tile : grounds) {

            if (bounds.overlaps(tile)) {

                return true;
            }
        }

        return false;
    }

    private boolean hasLineOfSight(
        Player player,
        FirstMap map
    ) {

        Vector2 start =
            new Vector2(
                bounds.x,
                bounds.y
            );

        Vector2 end =
            new Vector2(
                player.getBounds().x,
                player.getBounds().y
            );

        Vector2 dir =
            end.cpy()
                .sub(start)
                .nor();

        float distance =
            start.dst(end);

        for (
            float i = 0;
            i < distance;
            i += 16f
        ) {

            Rectangle probe =
                new Rectangle(
                    start.x + dir.x * i,
                    start.y + dir.y * i,
                    4,
                    4
                );

            for (Rectangle tile :
                map.getGroundTiles()
            ) {

                if (
                    probe.overlaps(tile)
                ) {

                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void update(
        float delta,
        FirstMap map,
        Player player
    ) {

        super.update(
            delta,
            map,
            player
        );



        if (dead) {

            velocityY += GRAVITY * delta;

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
                        MossFlyAnimator.MossflyState.DEATH_LAND;

                    break;
                }
            }

            return;
        }

        stateTime += delta;

        if (!playerDetected) {

            currentState =
                MossFlyAnimator.MossflyState.SHAKE;

            idleShakeTimer += delta;

            if (canSeePlayer(player)) {

                playerDetected = true;

                currentState =
                    MossFlyAnimator.MossflyState.APEAR;

                stateTime = 0f;
            }

            return;
        }

        if (
            currentState ==
                MossFlyAnimator.MossflyState.APEAR
        ) {

            if (stateTime >= 0.3f) {

                currentState =
                    MossFlyAnimator.MossflyState.FLYING;

                stateTime = 0f;
            }

            return;
        }

        if (
            currentState ==
                MossFlyAnimator.MossflyState.FLYING
        ) {

            Rectangle playerBounds =
                player.getBounds();

            float targetX =
                playerBounds.x +
                    playerBounds.width / 2f;

            float targetY =
                playerBounds.y +
                    playerBounds.height / 2f;

            float oldX = bounds.x;
            float oldY = bounds.y;

            followTarget(
                targetX,
                targetY,
                delta
            );

            if (
                isCollidingWall(
                    map.getGroundTiles()
                )
            ) {

                bounds.x = oldX;
                bounds.y = oldY;
            }

            direction =
                targetX > bounds.x
                    ? 1
                    : -1;
        }

    }

    @Override
    public void takeDamage(
        int damage
    ) {

        if (dead)
            return;

        hp -= damage;

        if (hp <= 0) {

            die();
        }
    }

    private void die() {

        dead = true;

        notifyDeath();

        velocityY = 500f;

        currentState =
            MossFlyAnimator.MossflyState.DEATH_AIR;

        stateTime = 0f;
    }

    @Override
    public void stopMovement() {

        velocityX = 0f;
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

        float shakeOffsetX = 0f;

        if (
            currentState ==
                MossFlyAnimator.MossflyState.SHAKE
        ) {

            shakeOffsetX =
                MathUtils.sin(
                    idleShakeTimer * 12f
                ) * 2f;
        }

        batch.draw(
            frame,

            direction == -1
                ? bounds.x + shakeOffsetX
                : bounds.x + WIDTH + shakeOffsetX,

            bounds.y,

            direction == -1
                ? WIDTH
                : -WIDTH,

            HEIGHT
        );
    }
}
