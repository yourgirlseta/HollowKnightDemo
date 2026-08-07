package com.yourgirlseta.hollowKnight.model.enemy.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.enemy.Enemy;
import com.yourgirlseta.hollowKnight.model.enums.FalseKnightState;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public class FalseKnight extends Enemy {

    private final FalseKnightMovement movement = new FalseKnightMovement();
    private Array<ShockWave> shockWaves = new Array<>();
    private float groundY;
    private float stateTime;
    private final int maxHp = 6;

    private boolean facingRight = false;
    private float chargeTargetX;

    private boolean stunned = false;
    private boolean phase2 = false;

    private float stunTimer;

    private float runDuration;
    private int runDirection;

    private FalseKnightState state =
        FalseKnightState.IDLE;

    private final FalseKnightAnimator animator;

    private final FalseKnightAI ai;

    private final FalseKnightCombat combat;

    public boolean requestShake;
    private boolean deathAnimationFinished = false;
    private Animation<TextureRegion> shockAnimation;

    public boolean isDeathAnimationFinished() {
        return deathAnimationFinished;
    }

    public boolean consumeShakeRequest() {
        if (!requestShake) return false;
        requestShake = false;
        return true;
    }

    public void requestCameraShake() {
        requestShake = true;
    }

    public boolean isHeavyMoving() {
        return state == FalseKnightState.RUN || state == FalseKnightState.ATTACK;
    }

    public FalseKnight(float x, float y, float groundY) {


        bounds = new Rectangle(x, y, 180, 200);

        hp = maxHp;
        speed = 100;

        this.groundY = groundY;

        animator = new FalseKnightAnimator();

        ai = new FalseKnightAI();
        combat = new FalseKnightCombat(this);
        shockAnimation = loadShockAnimation();
    }

    private Animation<TextureRegion> loadShockAnimation() {
        Texture sheet = new Texture(Gdx.files.internal("spritesheet (2).png"));

        TextureRegion[][] temp = TextureRegion.split(sheet, 212, 217);

        TextureRegion[] frames = new TextureRegion[7];
        int index = 0;
        for (int r = 0; r < temp.length; r++) {
            for (int c = 0; c < temp[r].length; c++) {
                if (index < frames.length) {
                    frames[index++] = temp[r][c];
                }
            }
        }

        Animation<TextureRegion> anim = new Animation<>(0.05f, frames);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }

    @Override
    public void update(
        float delta,
        FirstMap map,
        Player player
    ) {

        movement.update(bounds, groundY, delta);
        stateTime += delta;

        if (isDead()) {

            updateState(delta, player);

            return;
        }

        if (hp <= maxHp / 2 && !phase2) {

            enterStun();
        }

        if (stunned) {

            stunTimer -= delta;

            if (stunTimer <= 0) {

                exitStun();
            }

            return;
        }

        for(int i=shockWaves.size-1;i>=0;i--){

            ShockWave wave = shockWaves.get(i);

            wave.update(delta);

            wave.checkHit(player);

            if(wave.shouldRemove()){

                shockWaves.removeIndex(i);

            }

        }

        float playerCenter = player.getBounds().x + player.getBounds().width / 2f;
        float bossCenter   = bounds.x + bounds.width / 2f;
        float distance     = Math.abs(playerCenter - bossCenter);

        chargeTargetX = playerCenter;

        boolean playerOnRight = playerCenter > bossCenter;

        if (state == FalseKnightState.IDLE) {

            if (playerOnRight != facingRight) {
                changeState(FalseKnightState.TURN);
            } else {
                changeState(ai.chooseState(distance, phase2));
            }
        }

        updateState(delta, player);
    }

    private void updateState(
        float delta,
        Player player
    ) {

        switch (state) {

            case RUN:
                movement.charge(bounds, runDirection, delta);

                float runPlayerCenter = player.getBounds().x + player.getBounds().width / 2f;
                float runBossCenter   = bounds.x + bounds.width / 2f;
                float runDistance     = Math.abs(runPlayerCenter - runBossCenter);

                if (runDistance < 230f) {
                    changeState(FalseKnightState.ATTACK_ANTIC);
                    return;
                }

                if (stateTime >= runDuration) {
                    changeState(FalseKnightState.IDLE);
                    return;
                }
                break;
            case ATTACK:

                combat.maceSlam(player);

                break;

            case JUMP:

                if(movement.isGrounded()){
                    requestShake = true;
                    changeState(FalseKnightState.LAND);

                }

                break;

            case JUMP_ATTACK:

                if (movement.isGrounded()) {
                    requestShake = true;
                    combat.jumpSlam(player);

                    if (phase2) {
                        shockWaves.add(new ShockWave(bounds.x, bounds.y, -1, shockAnimation));
                        shockWaves.add(new ShockWave(bounds.x + bounds.width, bounds.y, 1, shockAnimation));

                    }

                    changeState(FalseKnightState.LAND);
                    return;
                }

                break;

            case STUN_RECOVER:
                break;

            default:
                break;
        }

        if (animator.isFinished(state, stateTime)) {

            combat.resetAttack();

            switch (state) {

                case RUN_ANTIC:
                    changeState(FalseKnightState.RUN);
                    return;

                case ATTACK_ANTIC:
                    changeState(FalseKnightState.ATTACK);
                    return;

                case ATTACK:
                    changeState(FalseKnightState.ATTACK_RECOVER);
                    return;

                case ATTACK_RECOVER:
                    changeState(FalseKnightState.IDLE);
                    return;

                case STUN:
                    return;

                case DEATH_FALL:
                    changeState(FalseKnightState.DEATH_HIT);
                    return;

                case DEATH_HIT:
                    changeState(FalseKnightState.DEATH_LAND);
                    return;

                case DEATH_LAND:
                    deathAnimationFinished = true;
                    return;


                case TURN:
                    facingRight = !facingRight;
                    changeState(FalseKnightState.IDLE);
                    return;

                default:
                    changeState(FalseKnightState.IDLE);
            }
        }
    }

    private void changeState(FalseKnightState newState) {

        state = newState;
        if (newState == FalseKnightState.TURN) {
            movement.stop();
        }

        if (newState == FalseKnightState.RUN) {

            runDirection = facingRight ? 1 : -1;
            runDuration = 1.3f;
        }

        if (newState == FalseKnightState.JUMP) {
            movement.jump(facingRight);
        }

        if (newState == FalseKnightState.JUMP_ATTACK) {
            movement.powerJumpToward(bounds, chargeTargetX);
        }
        stateTime = 0f;
    }

    private void enterStun() {

        stunned = true;
        movement.stop();
        phase2 = true;

        stunTimer = 6f;

        changeState(FalseKnightState.STUN);
    }

    private void exitStun() {

        stunned = false;

        movement.setMoveSpeed(350);
        movement.setChargeSpeed(900);
        movement.setJumpSpeed(1000);

        changeState(
            FalseKnightState.STUN_RECOVER
        );
    }

    @Override
    public void render(
        SpriteBatch batch
    ) {

        TextureRegion frame =
            animator.getFrame(
                state,
                stateTime
            );

        float scaleX = facingRight ? -1f : 1f;
        batch.draw(frame, bounds.x, bounds.y,
            bounds.width / 2f, bounds.height / 2f,
            bounds.width, bounds.height,
            scaleX, 1f, 0f);

        for (ShockWave wave : shockWaves) {

            TextureRegion frameShock = wave.getCurrentFrame();
            boolean flip = wave.getDirection() < 0;

            float vw = wave.getVisualWidth();
            float vh = wave.getVisualHeight();

            float drawX = wave.getBounds().x + wave.getBounds().width / 2f - vw / 2f;
            float drawY = wave.getBounds().y + wave.getBounds().height / 2f - vh / 2f;

            batch.draw(
                frameShock,
                drawX, drawY,
                vw / 2f, vh / 2f,
                vw, vh,
                flip ? -1f : 1f, 1f,
                0f
            );
        }
    }

    @Override
    public void stopMovement() {
        movement.stop();
    }

    private int poise = 0;
    private static final int POISE_MAX = 4;

    @Override
    public void takeDamage(int damage){
        if (dead) return;

        if (stunned) {
            hp -= damage;
            if (hp <= 0) {
                dead = true;

                notifyDeath();

                changeState(FalseKnightState.DEATH_FALL);
            }
            return;
        }

        poise += damage;
        if (poise >= POISE_MAX) {
            poise = 0;
            enterStun();
        }
    }

    public Rectangle getHurtBox() {
        float visibleHeightRatio = 0.528f;
        float hurtHeight = bounds.height * visibleHeightRatio;

        return new Rectangle(
            bounds.x + 45,
            bounds.y,
            130,
            hurtHeight
        );
    }


    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public int getHealth() {
        return hp;
    }

    public boolean isPhase2() {
        return phase2;
    }

    public boolean isStunned() {
        return stunned;
    }
}
