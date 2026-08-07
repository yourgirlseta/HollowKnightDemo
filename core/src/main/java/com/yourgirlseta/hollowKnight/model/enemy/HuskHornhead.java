package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public class HuskHornhead
    extends GroundEnemy {

    private static final float HITBOX_WIDTH = 80f;
    private static final float HITBOX_HEIGHT = 120f;

    private static final float WIDTH =
        239f;

    private static final float HEIGHT =
        219f;

    private static final float GRAVITY =
        -1800f;

    private static final float WALK_DURATION =
        3f;

    private static final float IDLE_DURATION =
        1.5f;

    private static final float CHARGE_SPEED =
        200f;

    private static final float ATTACK_COOLDOWN =
        2f;

    private final HuskHornheadAnimator
        animator;

    private float stateTime = 0f;

    private float velocityY = 0f;

    private float velocityX = 0f;

    private boolean isTurning = false;


    private float walkTimer = 0f;

    private float idleTimer = 0f;

    private float attackCooldownTimer = 0f;

    private int chargeDirection = 1;

    private HuskHornheadAnimator
        .HuskState currentState =
        HuskHornheadAnimator
            .HuskState.WALKING;

    private HuskHornheadAnimator
        .HuskState previousState =
        HuskHornheadAnimator
            .HuskState.WALKING;

    public HuskHornhead(
        float x,
        float y,
        float speed,
        int hp,
        int direction
    ) {

        this.bounds = new Rectangle(
            x,
            y,
            HITBOX_WIDTH,
            HITBOX_HEIGHT
        );

        this.speed = speed;

        this.hp = hp;

        this.direction = direction;

        animator =
            new HuskHornheadAnimator();
    }

    @Override
    public Rectangle getBounds() {

        return bounds;
    }

    @Override
    public void stopMovement() {

        velocityX = 0f;
    }

    public void update(
        float delta,
        FirstMap map,
        Player player
    ) {

        if (attackCooldownTimer > 0f) {

            attackCooldownTimer -= delta;
        }

        if (isKnocked) {

            bounds.x +=
                knockbackVelocityX * delta;

            knockbackVelocityX *= 0.90f;

            if (
                Math.abs(
                    knockbackVelocityX
                ) < 10f
            ) {

                knockbackVelocityX = 0f;

                isKnocked = false;
            }

            return;
        }

        if (dead) {

            if (
                currentState ==
                    HuskHornheadAnimator
                        .HuskState
                        .DEATH_AIR
            ) {

                velocityY +=
                    GRAVITY * delta;

                bounds.y +=
                    velocityY * delta;

                for (Rectangle tile :
                    map.getGroundTiles()) {

                    if (
                        bounds.overlaps(tile)
                            &&
                            velocityY <= 0f
                    ) {

                        bounds.y =
                            tile.y +
                                tile.height;

                        velocityY = 0f;

                        currentState =
                            HuskHornheadAnimator
                                .HuskState
                                .DEATH_LAND;

                        stateTime = 0f;

                        break;
                    }
                }
            }

            return;
        }

        velocityY +=
            GRAVITY * delta;

        bounds.y +=
            velocityY * delta;

        boolean grounded = false;

        Rectangle feet =
            new Rectangle(
                bounds.x + 20f,
                bounds.y,
                bounds.width - 40f,
                16f
            );

        for (Rectangle tile :
            map.getGroundTiles()) {

            if (
                feet.overlaps(tile)
                    &&
                    velocityY <= 0f
            ) {

                bounds.y =
                    tile.y +
                        tile.height;

                velocityY = 0f;

                grounded = true;

                break;
            }
        }

        if (isTurning) {

            stateTime += delta;

            if (
                animator.isTurnFinished(
                    stateTime
                )
            ) {

                isTurning = false;

                direction *= -1;

                currentState =
                    HuskHornheadAnimator
                        .HuskState
                        .WALKING;

                bounds.x += direction * 20f;

                stateTime = 0f;
            }

            return;
        }

        if (
            (
                currentState ==
                    HuskHornheadAnimator
                        .HuskState
                        .WALKING

                    ||

                    currentState ==
                        HuskHornheadAnimator
                            .HuskState
                            .IDLE
            )

                &&

                attackCooldownTimer <= 0f
        ) {

            if (
                getVisionBox()
                    .overlaps(
                        player.getBounds()
                    )
            ) {

                currentState =
                    HuskHornheadAnimator
                        .HuskState
                        .ATTACK_ANTICIPATE;

                chargeDirection =
                    player
                        .getBounds()
                        .x > bounds.x
                        ? 1
                        : -1;

                direction =
                    chargeDirection;

                stateTime = 0f;
            }
        }

        if (
            currentState ==
                HuskHornheadAnimator
                    .HuskState
                    .WALKING
        ) {

            walkTimer += delta;

            float oldX =
                bounds.x;

            bounds.x +=
                speed *
                    direction *
                    delta;

            boolean wall =
                isCollidingWithWall(
                    map.getGroundTiles()
                );

            boolean edge =
                grounded
                    &&
                    !hasGroundAhead(
                        map.getGroundTiles(),
                        direction
                    );

            if (wall || edge) {

                bounds.x = oldX;

                isTurning = true;

                currentState =
                    HuskHornheadAnimator
                        .HuskState
                        .TURN;

                stateTime = 0f;

                return;
            }

            if (
                walkTimer >=
                    WALK_DURATION
            ) {

                currentState =
                    HuskHornheadAnimator
                        .HuskState
                        .IDLE;

                walkTimer = 0f;

                idleTimer = 0f;

                stateTime = 0f;
            }
        }

        else if (
            currentState ==
                HuskHornheadAnimator
                    .HuskState
                    .IDLE
        ) {

            idleTimer += delta;

            if (
                idleTimer >=
                    IDLE_DURATION
            ) {

                currentState =
                    HuskHornheadAnimator
                        .HuskState
                        .WALKING;

                idleTimer = 0f;

                stateTime = 0f;
            }
        }


        else if (
            currentState ==
                HuskHornheadAnimator
                    .HuskState
                    .ATTACK_ANTICIPATE
        ) {

            if (
                animator
                    .isAttackAnticipateFinished(
                        stateTime
                    )
            ) {

                currentState =
                    HuskHornheadAnimator
                        .HuskState
                        .ATTACK_LUNGE;

                stateTime = 0f;
            }
        }

        else if (
            currentState ==
                HuskHornheadAnimator
                    .HuskState
                    .ATTACK_LUNGE
        ) {

            float oldX =
                bounds.x;

            bounds.x +=
                CHARGE_SPEED *
                    chargeDirection *
                    delta;

            boolean hitWall =
                isCollidingWithWall(
                    map.getGroundTiles()
                );

            boolean noGround =
                grounded
                    &&
                    !hasGroundAhead(
                        map.getGroundTiles(),
                        chargeDirection
                    );

            if (
                hitWall
                    ||
                    noGround
            ) {

                bounds.x = oldX;

                currentState =
                    HuskHornheadAnimator
                        .HuskState
                        .IDLE;

                attackCooldownTimer =
                    ATTACK_COOLDOWN;

                idleTimer = 0f;

                stateTime = 0f;
            }
        }

        if (currentState != previousState) {

            stateTime = 0f;

            previousState = currentState;

        } else {

            stateTime += delta;
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

        velocityY = 450f;

        currentState =
            HuskHornheadAnimator
                .HuskState
                .DEATH_AIR;

        stateTime = 0f;
    }

    private Rectangle getVisionBox() {

        if (direction == 1) {

            return new Rectangle(
                bounds.x +
                    bounds.width,
                bounds.y,
                350f,
                120f
            );
        }

        return new Rectangle(
            bounds.x - 350f,
            bounds.y,
            350f,
            120f
        );
    }

    private boolean isCollidingWithWall(
        Array<Rectangle> grounds
    ) {

        Rectangle probe;

        if (direction == 1) {

            probe = new Rectangle(
                bounds.x + bounds.width,
                bounds.y + 20f,
                6f,
                bounds.height - 40f
            );

        } else {

            probe = new Rectangle(
                bounds.x - 6f,
                bounds.y + 20f,
                6f,
                bounds.height - 40f
            );
        }

        for (Rectangle tile : grounds) {

            if (probe.overlaps(tile)) {

                return true;
            }
        }

        return false;
    }

    private boolean hasGroundAhead(
        Array<Rectangle> grounds,
        int dir
    ) {

        float checkX =
            dir == 1
                ? bounds.x + bounds.width - 10f
                : bounds.x + 10f;

        float checkY =
            bounds.y - 4f;

        Rectangle probe =
            new Rectangle(
                checkX,
                checkY,
                4f,
                4f
            );

        for (Rectangle tile :
            grounds) {

            if (
                probe.overlaps(tile)
            ) {

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

            bounds.x - 80f,
            bounds.y - 40f,

            WIDTH / 2f,
            0f,

            WIDTH,
            HEIGHT,

            direction == 1
                ? -1f
                : 1f,

            1f,

            0f
        );
    }
}

