package com.yourgirlseta.hollowKnight.model.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.model.GameData;
import com.yourgirlseta.hollowKnight.model.charms.CharmManager;
import com.yourgirlseta.hollowKnight.model.enums.SpellType;
import com.yourgirlseta.hollowKnight.model.spells.SpellManager;
import com.yourgirlseta.hollowKnight.model.enums.AttackDirection;
import com.yourgirlseta.hollowKnight.model.enums.PlayerState;
import com.yourgirlseta.hollowKnight.model.map.BreakableWall;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;
import com.yourgirlseta.hollowKnight.model.settingsUtils.ControlsManager;

public class Player {
    private Main game;
    private SoulManager soulManager;
    private final CharmManager charmManager;
    private SpellManager spellManager;
    private SpellType spellType;

    private float castTimer = 0f;

    private static final float CAST_DURATION = 0.35f;
    private static final int SPELL_COST = 33;
    private boolean focusing = false;
    private float focusTimer = 0f;
    private static final float FOCUS_DURATION = 1.5f;
    private static final int FOCUS_COST = 33;
    private float hurtTimer = 0f;
    private static final float HURT_DURATION = 0.2f;
    private static final float KNOCKBACK_X = 500f;
    private static final float KNOCKBACK_Y = 450f;
    private float blinkTimer = 0f;
    private static final float BLINK_INTERVAL = 0.1f;
    private float deathTimer = 0f;
    private static final float RESPAWN_DELAY = 2f;
    private boolean isDead = false;
    private HealthHud healthHud;
    private float stateTime = 0f;
    private PlayerState previousState = PlayerState.IDLE;
    private PlayerAnimator animator;
    private Vector2 respawnPoint;
    public Rectangle bounds;
    public float velocityX, velocityY;
    public PlayerState currentState = PlayerState.IDLE;
    private AttackDirection attackDirection = AttackDirection.FORWARD;
    private static final float MOVE_SPEED = 400f;
    private static final float DASH_SPEED = 900f;
    private static final float ATTACK_DURATION = 0.2f;
    private static final float DASH_DURATION = 0.15f;
    private boolean isDoubleJumping = false;
    private boolean isJumping = false;
    private boolean canDash = true;
    private static final float JUMP_FORCE = 720f;
    private static final float GRAVITY = -1300f;
    private static final float JUMP_CUTOFF_MULTIPLIER = 0.6f;
    private static final float FALL_GRAVITY_MULTIPLIER = 1.3f;
    private static final float LOW_JUMP_GRAVITY_MULTIPLIER = 1.7f;
    private static final float WALL_SLIDE_SPEED = -120f;
    private boolean isTouchingWall = false;
    private int wallDirection = 0;
    private boolean isWallSliding = false;
    private boolean attackHasHit = false;
    private float attackTimer = 0f;
    private float dashTimer = 0f;
    private boolean isGrounded = false;
    private boolean canDoubleJump = false;
    private boolean jumpCutoffApplied = false;
    private boolean attackStarted = false;
    private boolean canMove = true;
    private int facingDirection = 1;
    private static final float NOCLIP_SPEED = 900f;
    private float dashCooldownTimer = 0f;
    private static final float DASH_COOLDOWN = 0.6f;
    private final ControlsManager controls;
    private final GameData gameData;
    private final Vector2 lastSafePosition = new Vector2();
    private boolean hasSafePosition = false;
    private boolean pendingHazardRespawn = false;
    private static final float HAZARD_HURT_DURATION = 0.3f;


    public Player(float x, float y, ControlsManager controls, HealthHud healthHud, SoulManager soulManager, SpellManager spellManager, CharmManager charmManager, Main game, GameData gameData) {
        this.animator = new PlayerAnimator();
        this.bounds = new Rectangle(x, y, 48, 64);
        this.controls = controls;
        this.healthHud = healthHud;
        this.soulManager = soulManager;
        this.spellManager = spellManager;
        this.charmManager = charmManager;
        this.game = game;
        this.gameData = gameData;
    }

    public void setSafePosition(float x, float y) {
        lastSafePosition.set(x, y);
        hasSafePosition = true;
    }

    public Vector2 getSafePosition() {
        return lastSafePosition;
    }

    public boolean hasSafePosition() {
        return hasSafePosition;
    }

    public void update(float delta, FirstMap map) {
        if (!canMove) {
            velocityX = 0f;
            velocityY = 0f;
            currentState = PlayerState.IDLE;

            animator.update(
                stateTime,
                currentState,
                attackDirection,
                facingDirection
            );

            return;
        }

        if (isDead) {
            currentState = PlayerState.DEATH;
            stateTime += delta;
            deathTimer += delta;

            if (deathTimer >= RESPAWN_DELAY) {

                respawn(respawnPoint.x, respawnPoint.y);
                restAndHeal();
                map.respawnEnemies();
            }

            animator.update(
                stateTime,
                currentState,
                attackDirection,
                facingDirection
            );

            return;
        }

        if (castTimer > 0f) {

            castTimer -= delta;

            currentState = PlayerState.CASTING;

            stateTime += delta;

            animator.update(
                stateTime,
                currentState,
                attackDirection,
                facingDirection
            );

            return;
        }

        if (hurtTimer > 0f) {

            hurtTimer -= delta;

            if (pendingHazardRespawn) {
                if (hurtTimer <= 0f) {
                    pendingHazardRespawn = false;
                    if (hasSafePosition()) {
                        Vector2 safe = getSafePosition();
                        respawn(safe.x, safe.y);
                    }
                }
                return;
            }

            applyGravity(delta);

            move(delta);

            resolveCollisions(
                map.getGroundTiles(),
                map.getBreakableWalls()
            );

            updateState();

            animator.update(
                stateTime,
                currentState,
                attackDirection,
                facingDirection
            );

            return;
        }

        updateTimers(delta);
        updateDashCooldown(delta);
        updateFocus(delta);
        if (focusing) {
            currentState = PlayerState.FOCUSING;

            stateTime += delta;

            animator.update(
                stateTime,
                currentState,
                attackDirection,
                facingDirection
            );

            return;
        }
        handleActionInputs();
        if (healthHud.isNoclip()) {
            updateNoclip(delta);

            return;
        }
        handleMovementInput();
        handleJumpInput();

        applyGravity(delta);
        applyDashMovement();

        updateWallSlide();

        move(delta);
        resolveCollisions(
            map.getGroundTiles(),
            map.getBreakableWalls()
        );

        if (healthHud.isInvincible()) {
            blinkTimer += delta;
        } else {
            blinkTimer = 0f;
        }
        updateState();
        if (currentState != previousState) {
            stateTime = 0f;
            previousState = currentState;
        } else {
            stateTime += delta;
        }

        animator.update(stateTime, currentState, attackDirection, facingDirection);
    }

    private static final float GROUND_CHECK_EPSILON = 2f;

    private void checkGrounded(Array<Rectangle> grounds, Array<BreakableWall> breakableWalls) {
        Rectangle feet = new Rectangle(
            bounds.x + 4,
            bounds.y - GROUND_CHECK_EPSILON,
            bounds.width - 8,
            GROUND_CHECK_EPSILON
        );

        isGrounded = false;

        for (Rectangle tile : grounds) {
            if (feet.overlaps(tile)) {
                isGrounded = true;
                break;
            }
        }

        if (!isGrounded) {
            for (BreakableWall wall : breakableWalls) {
                if (wall.isBroken()) continue;

                if (feet.overlaps(wall.bounds)) {
                    isGrounded = true;
                    break;
                }
            }
        }
    }

    private void updateFocus(float delta) {
        boolean holdingFocus = controls.isFocusPressed();

        if (!holdingFocus ||
                !isGrounded
                ||
                velocityX != 0f
                ||
                attackTimer > 0f
                ||
                dashTimer > 0f
                ||
                healthHud.isFullHealth()
        ) {
            cancelFocus();
            return;
        }

        if (soulManager.getSoul() < FOCUS_COST) {
            cancelFocus();
            return;
        }

        if (!focusing) {
            focusing = true;
            focusTimer = 0f;
            attackTimer = 0f;
            dashTimer = 0f;
            velocityX = 0f;
            velocityY =0f;
        }

        focusTimer += delta;

        if (focusTimer >= getFocusDuration()) {
            soulManager.consumeSoul(FOCUS_COST);

            healthHud.heal();
            game.getAudioManager().playFocus();
            cancelFocus();
        }
    }

    private void cancelFocus() {
        focusing = false;
        focusTimer = 0f;
    }

    private void updateNoclip(float delta) {
        velocityX = 0f;
        velocityY = 0f;
        float speed = NOCLIP_SPEED;

        if (controls.isMoveLeftPressed()) {
            velocityX = -speed;
            facingDirection = -1;
        }

        if (controls.isMoveRightPressed()) {
            velocityX = speed;
            facingDirection = 1;
        }

        if (controls.isJumpPressed()) {
            velocityY = speed;
        }

        if (controls.isMoveDownPressed()) {
            velocityY = -speed;
        }

        bounds.x += velocityX * delta;

        bounds.y += velocityY * delta;

        currentState = PlayerState.IDLE;

        stateTime += delta;

        animator.update(
            stateTime,
            currentState,
            attackDirection,
            facingDirection
        );
    }

    private void updateTimers(float delta) {

        if (attackTimer > 0f) {

            attackTimer -= delta;

            if (attackTimer < 0f) {

                attackTimer = 0f;
            }
        }

        if (attackTimer <= 0f) {

            attackHasHit = false;
        }

        if (dashTimer > 0f) {

            dashTimer -= delta;

            if (dashTimer < 0f) {

                dashTimer = 0f;
            }
        }
    }
    private void updateWallSlide() {
        boolean holdingTowardWall =
            (wallDirection == 1 && controls.isMoveRightPressed()) ||
                (wallDirection == -1 && controls.isMoveLeftPressed());

        isWallSliding =
            !isGrounded &&
                isTouchingWall &&
                holdingTowardWall &&
                velocityY < 0f &&
                dashTimer <= 0f &&
                attackTimer <= 0f;

        if (isWallSliding && velocityY < WALL_SLIDE_SPEED) {
            velocityY = WALL_SLIDE_SPEED;
        }
    }


    private void handleActionInputs() {
        if (focusing) {
            return;
        }

        if (controls.isAttackJustPressed() && attackTimer <= 0f && dashTimer <= 0f) {
            attackTimer =
                getAttackDuration();
            game.getAudioManager().playSlashSound();

            attackHasHit = false;

            attackStarted = true;

            if (
                !isGrounded
                    &&
                    controls.isMoveUpPressed()
            ) {

                attackDirection =
                    AttackDirection.UP;
            }

            else if (
                !isGrounded
                    &&
                    controls.isMoveDownPressed()
            ) {

                attackDirection =
                    AttackDirection.DOWN;
            }

            else {

                attackDirection =
                    AttackDirection.FORWARD;
            }
        }

        if (
            controls.isDashJustPressed()
                &&
                canDash
                &&
                dashTimer <= 0f
                &&
                attackTimer <= 0f
        ) {

            dashTimer =
                getDashDuration();

            canDash = false;
        }

        if (
            controls.isCastPressed()
                &&
                soulManager.getSoul() >= SPELL_COST
                &&
                castTimer <= 0f
        ) {

            castTimer = CAST_DURATION;


            if (
                controls.isMoveUpPressed()
            ) {

                spellType =
                    SpellType.HOWLING_WRAITHS;

            }

            // =====================
            // FORWARD SPELL
            // =====================

            else {

                spellType =
                    SpellType.VENGEFUL_SPIRIT;
            }

            startCast();
        }
    }

    public SpellType getSpellType() {

        return spellType;
    }

    private void startCast() {

        castTimer =
            CAST_DURATION;

        currentState =
            PlayerState.CASTING;

        velocityX = 0f;
        velocityY = 0f;

        soulManager.consumeSoul(
            SPELL_COST
        );

        spellManager.spawn(

            spellType,

            bounds.x +
                bounds.width / 2f,

            bounds.y + 20f,

            facingDirection
        );
    }

    public boolean didAttackStart() {

        return attackStarted;
    }

    public void consumeAttackStart() {

        attackStarted = false;
    }

    public boolean hasAttackHit() {

        return attackHasHit;
    }

    public void markAttackHit() {

        attackHasHit = true;
    }

    public AttackDirection getAttackDirection() {

        return attackDirection;
    }

    private float getAttackDuration() {
        return ATTACK_DURATION * charmManager.getAttackCooldownMultiplier();
    }

    private void updateDashCooldown(float delta) {
        if (!charmManager.hasDashmaster()) {
            return;
        }
        if (!canDash) {
            dashCooldownTimer += delta;
            float requiredCooldown = DASH_COOLDOWN * charmManager.getDashCooldownMultiplier();
            if (dashCooldownTimer >= requiredCooldown) {
                canDash = true;
            }
        } else {
            dashCooldownTimer = 0f;
        }
    }

    private float getFocusDuration() {
        return FOCUS_DURATION * charmManager.getFocusDurationMultiplier();
    }

    public boolean isDashing() {
        return dashTimer > 0f;
    }

    private float getDashDuration() {
        return DASH_DURATION * charmManager.getDashLengthMultiplier();
    }


    private void handleMovementInput() {
        if (focusing) {

            velocityX = 0f;

            return;
        }

        if (dashTimer > 0f) {
            return;
        }

        if (attackTimer > 0f) {
            velocityX = 0f;
            return;
        }

        if (controls.isMoveRightPressed()) {
            velocityX = MOVE_SPEED;
            facingDirection = 1;
        } else if (controls.isMoveLeftPressed()) {
            velocityX = -MOVE_SPEED;
            facingDirection = -1;
        } else {
            velocityX = 0f;
        }
    }

    private void handleJumpInput() {
        if (focusing) {
            return;
        }

        if (dashTimer > 0f) {
            return;
        }

        boolean jumpStartedThisFrame = false;

        if (controls.isJumpJustPressed()) {
            if (isGrounded) {
                velocityY = JUMP_FORCE;
                isGrounded = false;
                canDoubleJump = true;
                isDoubleJumping = false;
                jumpCutoffApplied = false;
                jumpStartedThisFrame = true;
            } else if (isTouchingWall) {
                velocityY = JUMP_FORCE;

                velocityX = 600f * -wallDirection;

                canDoubleJump = true;
                isDoubleJumping = false;
                jumpCutoffApplied = false;
                jumpStartedThisFrame = true;
            }else if (canDoubleJump) {
                velocityY = JUMP_FORCE;
                canDoubleJump = false;
                isDoubleJumping = true;
                jumpCutoffApplied = false;
                jumpStartedThisFrame = true;
            }
        }

        if (
            !jumpStartedThisFrame &&
                !controls.isJumpPressed() &&
                velocityY > 0f &&
                !jumpCutoffApplied
        ) {
            velocityY *= JUMP_CUTOFF_MULTIPLIER;
            jumpCutoffApplied = true;
        }
    }


    private void applyGravity(float delta) {
        if (isGrounded) {
            return;
        }

        float gravityMultiplier = 1f;

        if (velocityY < 0f) {
            gravityMultiplier = FALL_GRAVITY_MULTIPLIER;
        } else if (velocityY > 0f && !controls.isJumpPressed()) {
            gravityMultiplier = LOW_JUMP_GRAVITY_MULTIPLIER;
        }

        velocityY += GRAVITY * gravityMultiplier * delta;
    }


    private void applyDashMovement() {
        if (dashTimer > 0f) {
            velocityX = DASH_SPEED * facingDirection;
        }
    }

    public boolean isAttacking() {

        return attackTimer > 0f;
    }

    private static final float POGO_BOUNCE_FORCE = 1500f;

    public void pogoBounce() {
        velocityY = 0f;

        if (!charmManager.hasDashmaster()) {
            canDash = true;
        }

        velocityY = POGO_BOUNCE_FORCE;

        isGrounded = false;

        isJumping = true;

        isDoubleJumping = false;

        canDoubleJump = true;

        jumpCutoffApplied = false;

        isWallSliding = false;

        isTouchingWall = false;

        wallDirection = 0;
    }

    public boolean isPogoAttacking() {

        return
            attackTimer > 0f
                &&
                attackDirection ==
                    AttackDirection.DOWN
                &&
                velocityY < 0f;
    }

    public Rectangle getAttackHitbox() {
        if (attackDirection == AttackDirection.DOWN) {
            return new Rectangle(bounds.x, bounds.y - 20, bounds.width, 20);
        }

        if (facingDirection == 1) {
            return new Rectangle(bounds.x + bounds.width, bounds.y + 20, 35, 40);
        }

        return new Rectangle(bounds.x - 35, bounds.y + 20, 15, 40);
    }

    public void die() {
        if (isDead) return;

        isDead = true;
        this.gameData.playerDeaths++;
        deathTimer = 0f;

        currentState = PlayerState.DEATH;

        velocityX = 0f;
        velocityY = 0f;

        attackTimer = 0f;
        dashTimer = 0f;

        isWallSliding = false;
        isJumping = false;
        isDoubleJumping = false;
    }

    public void setPosition(float x, float y) {
        this.bounds.x = x;
        this.bounds.y = y;
    }


    public void respawn(float x, float y) {
        this.bounds.setPosition(x, y);
        this.velocityX = 0f;
        this.velocityY = 0f;
        this.isDead = false;
        this.currentState = PlayerState.IDLE;
        this.stateTime = 0f;
    }

    public void restAndHeal() {
        healthHud.resetHealth();
    }

    public boolean takeDamage() {

        if (isDead) {
            return false;
        }

        boolean didTakeDamage =
            healthHud.takeDamage();

        if (
            didTakeDamage
                &&
                healthHud.getCurrentHealth() <= 0
        ) {

            die();
        }

        return didTakeDamage
            &&
            !isDead;
    }


    private void move(float delta) {
        bounds.x += velocityX * delta;
        bounds.y += velocityY * delta;
    }

    private void resolveTileCollision(Rectangle tile) {

        if (!bounds.overlaps(tile)) return;

        float playerCenterX = bounds.x + bounds.width / 2f;
        float playerCenterY = bounds.y + bounds.height / 2f;

        float tileCenterX = tile.x + tile.width / 2f;
        float tileCenterY = tile.y + tile.height / 2f;

        float dx = playerCenterX - tileCenterX;
        float dy = playerCenterY - tileCenterY;

        float combinedHalfWidths =
            (bounds.width / 2f) + (tile.width / 2f);

        float combinedHalfHeights =
            (bounds.height / 2f) + (tile.height / 2f);

        float overlapX = combinedHalfWidths - Math.abs(dx);
        float overlapY = combinedHalfHeights - Math.abs(dy);

        if (overlapX < overlapY) {

            if (dx > 0) {
                bounds.x += overlapX;
                wallDirection = -1;
            } else {
                bounds.x -= overlapX;
                wallDirection = 1;
            }

            velocityX = 0;
            isTouchingWall = true;
        }

        else {

            if (dy > 0) {
                bounds.y += overlapY;
                velocityY = 0;
                canDoubleJump = false;
                isDoubleJumping = false;
                if (!charmManager.hasDashmaster()) {
                    canDash = true;
                }
            } else {
                bounds.y -= overlapY;
                if (velocityY > 0)
                    velocityY = 0;
            }
        }
    }


    private void resolveCollisions(Array<Rectangle> grounds,
                                   Array<BreakableWall> breakableWalls) {

        isTouchingWall = false;

        for (Rectangle tile : grounds) {
            resolveTileCollision(tile);
        }

        for (BreakableWall wall : breakableWalls) {
            if (wall.isBroken()) continue;
            resolveTileCollision(wall.bounds);
        }

        checkGrounded(grounds, breakableWalls);
        if (isGrounded) {
            setSafePosition(bounds.x, bounds.y);
        }
    }


    private void updateState() {
        if (focusing) {

            currentState =
                PlayerState.FOCUSING;

            return;
        }

        if (dashTimer > 0f) {
            currentState = charmManager.hasSharpShadow()
                ? PlayerState.DASHING_SHARP_SHADOW
                : PlayerState.DASHING;
            return;
        }

        if (attackTimer > 0f) {
            currentState = PlayerState.ATTACKING;
            return;
        }

        if (isWallSliding) {
            currentState = PlayerState.WALL_SLIDING;
            return;
        }

        if (attackTimer > 0f) {
            currentState = PlayerState.ATTACKING;
            return;
        }

        if (!isGrounded) {
            if (isDoubleJumping) {
                currentState = velocityY > 0f ? PlayerState.DOUBLE_JUMPING : PlayerState.FALLING;
            } else {
                currentState = velocityY > 0f ? PlayerState.JUMPING : PlayerState.FALLING;
            }
            return;
        }

        currentState = velocityX != 0f ? PlayerState.RUNNING : PlayerState.IDLE;
    }

    public boolean takeHit(float sourceX) {
        cancelFocus();
        boolean tookDamage = takeDamage();

        if (!tookDamage) {return false;}

        cancelFocus();
        hurtTimer = HURT_DURATION;
        velocityY = KNOCKBACK_Y;
        if (bounds.x < sourceX) {
            velocityX = -KNOCKBACK_X;
        } else {
            velocityX = KNOCKBACK_X;
        }
        return true;
    }

    public boolean takeHazardHit() {
        cancelFocus();
        boolean tookDamage = takeDamage();

        if (!tookDamage) {
            return false;
        }

        hurtTimer = HAZARD_HURT_DURATION;
        velocityX = 0f;
        velocityY = 0f;
        pendingHazardRespawn = true;

        return true;
    }


    public int getSpirit() {
        return soulManager.getCurrentSoul();
    }
    public void setVelocityX(float velocityX) {this.velocityX = velocityX;}
    public boolean isFacingRight() {return facingDirection == 1;}
    public Rectangle getBounds() {return bounds;}
    public void setRespawnPoint(Vector2 point) {respawnPoint = new Vector2(point);}
    public void setCanMove(boolean canMove) {this.canMove = canMove;}
    public void render(SpriteBatch batch) {
        boolean visible = !healthHud.isInvincible() || ((int)(blinkTimer / BLINK_INTERVAL) % 2 == 0);
        if (!visible) {
            return;
        }
        animator.render(batch, bounds);
    }
}




