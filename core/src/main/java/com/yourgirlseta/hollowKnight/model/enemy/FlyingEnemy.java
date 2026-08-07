package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public abstract class FlyingEnemy
    extends Enemy {

    protected Vector2 velocity =
        new Vector2();

    protected float detectionRange =
        500f;

    protected boolean playerDetected =
        false;

    protected float flySpeed =
        220f;

    @Override
    public void update(
        float delta,
        FirstMap map,
        Player player
    ) {

        if (isKnocked) {

            bounds.x +=
                knockbackVelocityX * delta;

            knockbackVelocityX *=
                0.90f;

            if (
                Math.abs(
                    knockbackVelocityX
                ) < 10f
            ) {

                knockbackVelocityX = 0f;

                isKnocked = false;
            }
        }
    }

    protected void followTarget(
        float targetX,
        float targetY,
        float delta
    ) {

        Vector2 dir =
            new Vector2(
                targetX - bounds.x,
                targetY - bounds.y
            ).nor();

        velocity.x =
            MathUtils.lerp(
                velocity.x,
                dir.x * flySpeed,
                3f * delta
            );

        velocity.y =
            MathUtils.lerp(
                velocity.y,
                dir.y * flySpeed,
                3f * delta
            );

        bounds.x +=
            velocity.x * delta;

        bounds.y +=
            velocity.y * delta;
    }

    protected boolean canSeePlayer(
        Player player
    ) {

        float dx =
            player.getBounds().x -
                bounds.x;

        float dy =
            player.getBounds().y -
                bounds.y;

        float distance =
            Vector2.len(dx, dy);

        return distance <= detectionRange;
    }
}
