package com.yourgirlseta.hollowKnight.model.zote;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.enums.DialogueState;
import com.yourgirlseta.hollowKnight.model.enums.ZoteState;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;


public class Zote {

     private SettingsManager settingsManager;

    private Rectangle bounds;

    private ZoteAnimator animator;

    private DialogueManager dialogueManager;

    private ZoteState state;

    private float stateTime = 0f;

    private boolean facingRight = true;

    private float hitCooldown = 0f;

    private float angryTimer = 0f;

    private float rollSpeed = 420f;

    public Zote(
        float x,
        float y,
        SettingsManager settingsManager
    ) {

        bounds =
            new Rectangle(
                x,
                y,
                180,
                96
            );

        animator =
            new ZoteAnimator();

        dialogueManager =
            new DialogueManager(settingsManager);

        state =
            ZoteState.IDLE;
    }

    public void update(
        float delta,
        Player player
    ) {

        stateTime += delta;

        if (hitCooldown > 0f) {

            hitCooldown -= delta;
        }


        if (
            dialogueManager.isActive()
        ) {

            setState(
                ZoteState.TALKING
            );

            return;
        }

        boolean shouldFaceRight =
            player.bounds.x > bounds.x;

        if (
            shouldFaceRight != facingRight
                &&
                state != ZoteState.TURN
                &&
                state != ZoteState.ROLL
                &&
                state != ZoteState.ATTACK
                &&
                state != ZoteState.FALL
                &&
                state != ZoteState.GET_UP
        ) {

            setState(
                ZoteState.TURN
            );
        }

        switch (state) {

            case IDLE:
                updateIdle(delta);
                break;

            case TURN:
                updateTurn();
                break;

            case ROLL:
                updateRoll(delta);
                break;

            case ATTACK:
                updateAttack();
                break;

            case FALL:
                updateFall();
                break;

            case GET_UP:
                updateGetUp();
                break;

            case TALKING:
                updateTalking();
                break;
        }

        if (
            angryTimer > 0f
        ) {

            angryTimer -= delta;

            if (
                angryTimer <= 0f
            ) {

                setState(
                    ZoteState.IDLE
                );

                dialogueManager.setState(
                    DialogueState.PRECEPTS
                );
            }
        }
    }

    private void updateIdle(
        float delta
    ) {

        if (
            angryTimer > 0f
        ) {

            setState(
                ZoteState.ROLL
            );
        }
    }

    private void updateTurn() {

        if (
            animator.isFinished(
                ZoteState.TURN,
                stateTime
            )
        ) {

            facingRight =
                !facingRight;

            setState(
                ZoteState.IDLE
            );
        }
    }

    private void updateRoll(
        float delta
    ) {

        if (facingRight) {

            bounds.x +=
                rollSpeed * delta;

        } else {

            bounds.x -=
                rollSpeed * delta;
        }

        if (
            animator.isFinished(
                ZoteState.ROLL,
                stateTime
            )
        ) {

            setState(
                ZoteState.ATTACK
            );
        }
    }

    private void updateAttack() {

        if (
            animator.isFinished(
                ZoteState.ATTACK,
                stateTime
            )
        ) {

            setState(
                ZoteState.FALL
            );
        }
    }

    private void updateFall() {

        if (
            animator.isFinished(
                ZoteState.FALL,
                stateTime
            )
        ) {

            setState(
                ZoteState.GET_UP
            );
        }
    }

    private void updateGetUp() {

        if (
            animator.isFinished(
                ZoteState.GET_UP,
                stateTime
            )
        ) {

            setState(
                ZoteState.IDLE
            );
        }
    }


    private void updateTalking() {

    }

    public void hit() {

        if (
            hitCooldown > 0f
        ) {
            return;
        }

        hitCooldown = 1f;

        angryTimer = 5f;

        dialogueManager.setState(
            DialogueState.ANGRY
        );

        setState(
            ZoteState.ROLL
        );
    }

    public void render(
        SpriteBatch batch
    ) {

        TextureRegion frame =
            animator.getFrame(
                state,
                stateTime
            );

        if (facingRight) {

            batch.draw(
                frame,

                bounds.x,
                bounds.y,

                bounds.width,
                bounds.height
            );

        } else {

            batch.draw(
                frame,

                bounds.x + bounds.width,
                bounds.y,

                -bounds.width,
                bounds.height
            );
        }
    }

    public Rectangle getBounds() {

        return bounds;
    }

    public DialogueManager getDialogueManager() {

        return dialogueManager;
    }

    public void setState(
        ZoteState newState
    ) {

        if (
            state == newState
        ) {
            return;
        }

        state =
            newState;

        stateTime = 0f;
    }

    public boolean canInteract(
        Player player
    ) {

        return bounds.getCenter(
            new Vector2()
        ).dst(
            player.bounds.getCenter(
                new Vector2()
            )
        ) < 150f;
    }
}
