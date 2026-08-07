package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public class CrystalGuardian
    extends GroundEnemy {

    private LaserAnimator
        laserAnimator;

    private float
        laserStateTime = 0f;

    private int chargeDirection;

    private static final float HITBOX_WIDTH =
        90f;

    private static final float HITBOX_HEIGHT =
        140f;

    private static final float WIDTH =
        240f;

    private static final float HEIGHT =
        240f;

    private static final float GRAVITY =
        -1800f;

    private static final float VISION_RANGE =
        350f;

    private static final float LASER_RANGE =
        900f;

    private static final float RUN_SPEED =
        200f;

    private static final float ENRAGE_DURATION =
        2f;

    private static final float LASER_FIRE_TIME =
        0.35f;

    private final CrystalGuardianAnimator
        animator;

    private float stateTime = 0f;

    private float velocityY = 0f;

    private float enrageTimer = 0f;

    private boolean laserFired = false;

    private final float spawnX;

    private final float spawnY;

    private boolean
        showLaser = false;

    private CrystalGuardianAnimator.GuardianState
        currentState =
        CrystalGuardianAnimator
            .GuardianState
            .IDLE;

    private CrystalGuardianAnimator.GuardianState
        previousState =
        CrystalGuardianAnimator
            .GuardianState
            .IDLE;

    public CrystalGuardian(
        float x,
        float y,
        int hp,
        int direction
    ) {

        this.bounds =
            new Rectangle(
                x,
                y,
                HITBOX_WIDTH,
                HITBOX_HEIGHT
            );

        this.spawnX = x;

        this.spawnY = y;

        this.hp = hp;

        this.direction = direction;

        animator =
            new CrystalGuardianAnimator();

        laserAnimator =
            new LaserAnimator();
    }

    @Override
    public void update(
        float delta,
        FirstMap map,
        Player player
    ) {

        if (dead) {

            if (
                currentState ==
                   CrystalGuardianAnimator.GuardianState
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
                            CrystalGuardianAnimator.GuardianState
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

        Rectangle feet =
            new Rectangle(
                bounds.x + 10f,
                bounds.y,
                bounds.width - 20f,
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
                    tile.y +
                        tile.height;

                velocityY = 0f;

                break;
            }
        }

        if (
            currentState ==
                CrystalGuardianAnimator.GuardianState.IDLE
        ) {

            if (
                getVisionBox()
                    .overlaps(
                        player.getBounds()
                    )
            ) {

                direction =
                    player
                        .getBounds()
                        .x > bounds.x
                        ? 1
                        : -1;

                currentState =
                    CrystalGuardianAnimator.GuardianState
                        .SHOOT;

                laserFired = false;

                stateTime = 0f;
            }
        }

        else if (
            currentState ==
                CrystalGuardianAnimator.GuardianState.SHOOT
        ) {

            if (
                stateTime >=
                    LASER_FIRE_TIME
                    &&
                    !laserFired
            ) {

                fireLaser(player);

                laserFired = true;

                showLaser = true;
            }

            if (
                animator
                    .isShootFinished(
                        stateTime
                    )
            ) {

                currentState =
                    CrystalGuardianAnimator.GuardianState.RUN;

                enrageTimer = 0f;
                showLaser = false;
                stateTime = 0f;
            }
        }

        else if (
            currentState ==
                CrystalGuardianAnimator
                    .GuardianState
                    .RUN
        ) {

            enrageTimer += delta;

            direction =
                player.getBounds().x > bounds.x
                    ? 1
                    : -1;

            bounds.x +=
                RUN_SPEED *
                    direction *
                    delta;

            if (
                enrageTimer >=
                    ENRAGE_DURATION
            ) {

                currentState =
                    CrystalGuardianAnimator
                        .GuardianState
                        .RETURN;

                stateTime = 0f;
            }
        }

        else if (
            currentState ==
                CrystalGuardianAnimator.GuardianState.RETURN
        ) {

            float dirToSpawn =
                spawnX > bounds.x
                    ? 1f
                    : -1f;

            direction =
                dirToSpawn > 0
                    ? 1
                    : -1;

            bounds.x +=
                RUN_SPEED *
                    0.6f *
                    dirToSpawn *
                    delta;

            if (
                Math.abs(
                    spawnX - bounds.x
                ) < 10f
            ) {

                bounds.x = spawnX;

                currentState =
                    CrystalGuardianAnimator.GuardianState.IDLE;

                stateTime = 0f;
            }
        }

        if (
            currentState !=
                previousState
        ) {

            stateTime = 0f;

            previousState =
                currentState;

        } else {

            stateTime += delta;
        }
        laserStateTime += delta;
    }

    private void fireLaser(
        Player player
    ) {

        Rectangle laser;

        if (direction == 1) {

            laser =
                new Rectangle(
                    bounds.x,
                    bounds.y + 50f,
                    LASER_RANGE,
                    20f
                );

        } else {

            laser =
                new Rectangle(
                    bounds.x -
                        LASER_RANGE,
                    bounds.y + 50f,
                    LASER_RANGE,
                    20f
                );
        }

        if (
            laser.overlaps(
                player.getBounds()
            )
        ) {

            player.takeDamage();
        }
    }

    private Rectangle getVisionBox() {

        return new Rectangle(

            bounds.x - VISION_RANGE,

            bounds.y,

            VISION_RANGE * 2f,

            140f
        );
    }

    @Override
    public void takeDamage(int damage) {

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
            CrystalGuardianAnimator.GuardianState.DEATH_AIR;

        stateTime = 0f;
    }

    @Override
    public Rectangle getBounds() {

        return bounds;
    }

    @Override
    public void stopMovement() {

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

            bounds.x - 75f,
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

        if (
            showLaser
        ) {

            TextureRegion laserFrame =
                laserAnimator.getFrame(
                    laserStateTime
                );

            float startX =
                direction == 1
                    ? bounds.x + 40f
                    : bounds.x - LASER_RANGE;

            float y =
                bounds.y + 45f;

            float pieceWidth = 64f;

            for (
                float i = 0;
                i < LASER_RANGE;
                i += pieceWidth
            ) {

                batch.draw(

                    laserFrame,

                    direction == 1
                        ? startX + i
                        : startX + LASER_RANGE - i,

                    y,

                    direction == 1
                        ? pieceWidth
                        : -pieceWidth,

                    24f
                );
            }
        }
    }
}
