package com.yourgirlseta.hollowKnight.model.settingsUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.yourgirlseta.hollowKnight.model.enums.ControlAction;

public class ControlsManager {
    private final SettingsData settingsData;

    public ControlsManager(SettingsData settingsData) {
        this.settingsData = settingsData;
    }

    public boolean isMoveUpPressed() {
        return Gdx.input.isKeyPressed(settingsData.getMoveUp());
    }

    public boolean isMoveDownPressed() {
        return Gdx.input.isKeyPressed(settingsData.getMoveDown());
    }

    public boolean isMoveLeftPressed() {
        return Gdx.input.isKeyPressed(settingsData.getMoveLeft());
    }

    public boolean isMoveRightPressed() {
        return Gdx.input.isKeyPressed(settingsData.getMoveRight());
    }

    public boolean isJumpJustPressed() { return Gdx.input.isKeyJustPressed(settingsData.getJump()); }

    public boolean isJumpPressed() { return Gdx.input.isKeyPressed(settingsData.getJump()); }

    public boolean isFocusPressed() { return Gdx.input.isKeyPressed(settingsData.getFocus()); }

    public boolean isInventoryPressed() { return  Gdx.input.isKeyPressed(settingsData.getInventory()); }

    public boolean isInventoryJustPressed() {
        return Gdx.input.isKeyJustPressed(settingsData.getInventory());
    }

    public boolean isCastPressed() { return  Gdx.input.isKeyPressed(settingsData.getCast()); }

    public boolean isPauseMenuPressed() {
        return Gdx.input.isKeyJustPressed(settingsData.getPause());
    }

    public boolean isAttackJustPressed() {
        return Gdx.input.isKeyJustPressed(settingsData.getAttack());
    }

    public boolean isDashJustPressed() {
        return Gdx.input.isKeyJustPressed(settingsData.getDash());
    }

    public void resetControls() {
        settingsData.resetControls();
    }

    public void setMoveUp(int keycode) {
        settingsData.setMoveUp(keycode);
    }

    public void setMoveDown(int keycode) {
        settingsData.setMoveDown(keycode);
    }

    public void setMoveLeft(int keycode) {
        settingsData.setMoveLeft(keycode);
    }

    public void setMoveRight(int keycode) {
        settingsData.setMoveRight(keycode);
    }

    public void setJump(int keycode) {
        settingsData.setJump(keycode);
    }

    public void setAttack(int keycode) {
        settingsData.setAttack(keycode);
    }

    public void setDash(int keycode) {
        settingsData.setDash(keycode);
    }

    public void setFocus (int keycode) { settingsData.setFocus(keycode);}

    public void setInventory (int keycode) { settingsData.setInventory(keycode);}

    public void setCast (int keycode) { settingsData.setCast(keycode);}

    public void setPause (int keycode) { settingsData.setPause(keycode);}



    public boolean isKillAllCheat() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.K);
    }

    public boolean isBossTeleportCheat() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.B);
    }

    public boolean isFillSoulCheat() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.F);
    }

    public boolean isGodModeCheat() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.G);
    }

    public boolean isEmergencyHealCheat() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.E);
    }

    public boolean isNoclipCheat() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.N);
    }

    public int getKey(ControlAction action) {
        switch (action) {
            case MOVE_UP:
                return settingsData.getMoveUp();
            case MOVE_DOWN:
                return settingsData.getMoveDown();
            case MOVE_LEFT:
                return settingsData.getMoveLeft();
            case MOVE_RIGHT:
                return settingsData.getMoveRight();
            case ATTACK:
                return settingsData.getAttack();
            case JUMP:
                return settingsData.getJump();
            case DASH:
                return settingsData.getDash();
            case FOCUS:
                return settingsData.getFocus();
            case INVENTORY:
                return settingsData.getInventory();
            case CAST:
                return  settingsData.getCast();
            case PAUSE:
                return settingsData.getPause();
            default:
                throw new IllegalArgumentException("Unknown control action.");
        }
    }

    public void setKey(ControlAction action, int keycode) {
        switch (action) {
            case MOVE_UP:
                settingsData.setMoveUp(keycode);
                break;
            case MOVE_DOWN:
                settingsData.setMoveDown(keycode);
                break;
            case MOVE_LEFT:
                settingsData.setMoveLeft(keycode);
                break;
            case MOVE_RIGHT:
                settingsData.setMoveRight(keycode);
                break;
            case ATTACK:
                settingsData.setAttack(keycode);
                break;
            case JUMP:
                settingsData.setJump(keycode);
                break;
            case DASH:
                settingsData.setDash(keycode);
                break;
            case FOCUS:
                settingsData.setFocus(keycode);
                break;
            case INVENTORY:
                settingsData.setInventory(keycode);
                break;
            case CAST:
                settingsData.setCast(keycode);
                break;
            case PAUSE:
                settingsData.setPause(keycode);
                break;
            default:
                throw new IllegalArgumentException("Unknown control action.");
        }
    }
}
