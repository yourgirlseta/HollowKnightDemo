package com.yourgirlseta.hollowKnight.model.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public abstract class Enemy {

    protected Rectangle bounds;

    protected float knockbackVelocityX = 0f;
    protected boolean isKnocked = false;
    protected float speed;
    protected int hp;
    protected int direction = 1;

    protected boolean dead = false;

    public interface DeathListener {
        void onEnemyDied(Enemy enemy);
    }

    private DeathListener deathListener;
    protected boolean killCounted = false;

    public boolean isKillCounted() {
        return killCounted;
    }

    public void setKillCounted(boolean value) {
        killCounted = value;
    }

    public void setDeathListener(DeathListener listener) {
        this.deathListener = listener;
    }

    protected void notifyDeath() {
        if (deathListener != null) {
            deathListener.onEnemyDied(this);
        }
    }

    public boolean isDead() {
        return dead;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public abstract void takeDamage(
        int damage
    );

    public abstract void update(
        float delta,
        FirstMap map,
        Player player
    );

    public void applyKnockback(
        float force
    ) {

        knockbackVelocityX = force;

        isKnocked = true;
    }

    public abstract void stopMovement();

    public abstract void render(
        SpriteBatch batch
    );
}
