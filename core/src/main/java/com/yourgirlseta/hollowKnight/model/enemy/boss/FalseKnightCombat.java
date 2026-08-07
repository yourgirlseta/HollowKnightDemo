package com.yourgirlseta.hollowKnight.model.enemy.boss;


import com.badlogic.gdx.math.Rectangle;
import com.yourgirlseta.hollowKnight.model.character.Player;

public class FalseKnightCombat {

    private final FalseKnight boss;

    private boolean attackHit;

    public FalseKnightCombat(FalseKnight boss) {
        this.boss = boss;
    }

    public void resetAttack() {
        attackHit = false;
    }

    public void maceSlam(Player player) {

        if (attackHit) return;

        Rectangle bossBounds = boss.getHurtBox();

        float width = 100f;
        float height = 50f;

        float hitboxX;

        if (boss.isFacingRight()) {
            hitboxX = bossBounds.x;
        } else {
            hitboxX = bossBounds.x + - width - 60;
        }

        Rectangle hitbox = new Rectangle(
            hitboxX,
            bossBounds.y,
            width,
            height
        );

        if (hitbox.overlaps(player.getBounds())) {

            float center = bossBounds.x + bossBounds.width / 2f;

            boss.requestCameraShake();
            player.takeHit(center);

            attackHit = true;
        }
    }

    public void jumpSlam(Player player) {

        if (attackHit) return;

        Rectangle bossBounds = boss.getHurtBox();

        float width = 100f;
        float height = 50f;

        float hitboxX;

        if (boss.isFacingRight()) {
            hitboxX = bossBounds.x + 40;
        } else {
            hitboxX = bossBounds.x - 40;
        }

        Rectangle hitbox = new Rectangle(
            hitboxX,
            bossBounds.y,
            width,
            height
        );

        if (hitbox.overlaps(player.getBounds())) {

            float center = bossBounds.x + bossBounds.width / 2f;

            boss.requestCameraShake();

            player.takeHit(center);

            attackHit = true;
        }
    }
}
