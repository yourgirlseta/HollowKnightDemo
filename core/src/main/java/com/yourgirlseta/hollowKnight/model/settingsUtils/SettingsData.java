package com.yourgirlseta.hollowKnight.model.settingsUtils;

import com.badlogic.gdx.Input;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.enums.Theme;

public class SettingsData {

    private static final float MIN_VALUE = 0f;
    private static final float MAX_VALUE = 1f;

    private static final float DEFAULT_MUSIC_VOLUME = 1f;
    private static final boolean DEFAULT_MUSIC_MUTED = false;

    private static final float DEFAULT_SFX_VOLUME = 1f;
    private static final boolean DEFAULT_SFX_MUTED = false;

    private static final float DEFAULT_BRIGHTNESS = 1f;

    private static final Language DEFAULT_LANGUAGE = Language.ENGLISH;

    private static final int DEFAULT_MOVE_UP = Input.Keys.UP;
    private static final int DEFAULT_MOVE_DOWN = Input.Keys.DOWN;
    private static final int DEFAULT_MOVE_LEFT = Input.Keys.LEFT;
    private static final int DEFAULT_MOVE_RIGHT = Input.Keys.RIGHT;
    private static final int DEFAULT_ATTACK = Input.Keys.X;
    private static final int DEFAULT_JUMP = Input.Keys.SPACE;
    private static final int DEFAULT_DASH = Input.Keys.C;
    private static final int DEFAULT_FOCUS = Input.Keys.A;
    private static final int DEFAULT_INVENTORY = Input.Keys.I;
    private static final int DEFAULT_CAST = Input.Keys.TAB;
    private static final int DEFAULT_PAUSE = Input.Keys.ESCAPE;

    private float musicVolume = DEFAULT_MUSIC_VOLUME;
    private boolean musicMuted = DEFAULT_MUSIC_MUTED;

    private float sfxVolume = DEFAULT_SFX_VOLUME;
    private boolean sfxMuted = DEFAULT_SFX_MUTED;

    private float brightness = DEFAULT_BRIGHTNESS;

    private Language language = DEFAULT_LANGUAGE;

    private int moveUp = DEFAULT_MOVE_UP;
    private int moveDown = DEFAULT_MOVE_DOWN;
    private int moveLeft = DEFAULT_MOVE_LEFT;
    private int moveRight = DEFAULT_MOVE_RIGHT;
    private int attack = DEFAULT_ATTACK;
    private int jump = DEFAULT_JUMP;
    private int dash = DEFAULT_DASH;
    private int focus = DEFAULT_FOCUS;
    private int inventory = DEFAULT_INVENTORY;
    private int cast = DEFAULT_CAST;
    private int pause = DEFAULT_PAUSE;
    private static final Theme DEFAULT_THEME = Theme.DEFAULT;

    private Theme theme = DEFAULT_THEME;

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("Theme cannot be null.");
        }
        this.theme = theme;
    }

    public void toggleTheme() {
        theme = (theme == Theme.DEFAULT) ? Theme.ALTERNATE : Theme.DEFAULT;
    }

    public void resetTheme() {
        theme = DEFAULT_THEME;
    }

    public void resetAudio() {
        musicVolume = DEFAULT_MUSIC_VOLUME;
        musicMuted = DEFAULT_MUSIC_MUTED;
        sfxVolume = DEFAULT_SFX_VOLUME;
        sfxMuted = DEFAULT_SFX_MUTED;
    }

    public void resetControls() {
        moveUp = DEFAULT_MOVE_UP;
        moveDown = DEFAULT_MOVE_DOWN;
        moveLeft = DEFAULT_MOVE_LEFT;
        moveRight = DEFAULT_MOVE_RIGHT;
        attack = DEFAULT_ATTACK;
        jump = DEFAULT_JUMP;
        dash = DEFAULT_DASH;
        focus = DEFAULT_FOCUS;
        inventory = DEFAULT_INVENTORY;
        pause = DEFAULT_PAUSE;
    }

    public void resetBrightness() {
        brightness = DEFAULT_BRIGHTNESS;
    }

    public void resetLanguage() {
        language = DEFAULT_LANGUAGE;
    }

    public void resetAll() {
        resetAudio();
        resetControls();
        resetBrightness();
        resetLanguage();
        resetTheme();
    }

    public void toggleMusicMuted() {
        musicMuted = !musicMuted;
    }

    public void toggleSfxMuted() {
        sfxMuted = !sfxMuted;
    }

    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = clamp01(sfxVolume);
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = clamp01(musicVolume);
    }

    public void setBrightness(float brightness) {
        this.brightness = clamp01(brightness);
    }

    public void setLanguage(Language language) {
        if (language == null) {
            throw new IllegalArgumentException("Language cannot be null.");
        }

        this.language = language;
    }

    public void setAttack(int attack) {
        validateKeyIsNotUsedByOtherAction(attack, this.attack);
        this.attack = attack;
    }

    public void setDash(int dash) {
        validateKeyIsNotUsedByOtherAction(dash, this.dash);
        this.dash = dash;
    }

    public void setJump(int jump) {
        validateKeyIsNotUsedByOtherAction(jump, this.jump);
        this.jump = jump;
    }

    public void setMoveDown(int moveDown) {
        validateKeyIsNotUsedByOtherAction(moveDown, this.moveDown);
        this.moveDown = moveDown;
    }

    public void setMoveLeft(int moveLeft) {
        validateKeyIsNotUsedByOtherAction(moveLeft, this.moveLeft);
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(int moveRight) {
        validateKeyIsNotUsedByOtherAction(moveRight, this.moveRight);
        this.moveRight = moveRight;
    }

    public void setMoveUp(int moveUp) {
        validateKeyIsNotUsedByOtherAction(moveUp, this.moveUp);
        this.moveUp = moveUp;
    }

    public void setFocus(int focus) {
        validateKeyIsNotUsedByOtherAction(focus, this.focus);
        this.focus = focus;
    }

    public void setInventory(int inventory) {
        validateKeyIsNotUsedByOtherAction(inventory, this.inventory);
        this.inventory = inventory;
    }

    public void setCast(int cast) {
        validateKeyIsNotUsedByOtherAction(inventory, this.cast);
        this.cast = cast;
    }

    public void setPause(int pause) {
        validateKeyIsNotUsedByOtherAction(pause, this.pause);
        this.pause = pause;
    }

    public void setMusicMuted(boolean musicMuted) {
        this.musicMuted = musicMuted;
    }

    public void setSfxMuted(boolean sfxMuted) {
        this.sfxMuted = sfxMuted;
    }

    public boolean isSfxMuted() {
        return sfxMuted;
    }

    public boolean isMusicMuted() {
        return musicMuted;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getBrightness() {
        return brightness;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public int getAttack() {
        return attack;
    }

    public int getJump() {
        return jump;
    }

    public int getMoveDown() {
        return moveDown;
    }

    public int getMoveUp() {
        return moveUp;
    }

    public int getDash() {
        return dash;
    }

    public int getMoveLeft() {
        return moveLeft;
    }

    public Language getLanguage() {
        return language;
    }

    public int getMoveRight() {
        return moveRight;
    }

    public int getFocus() {return  focus;}

    public int getInventory() {return inventory;}

    public int getCast() {return cast;}

    public int getPause() {return pause;}

    private float clamp01(float value) {
        if (value < MIN_VALUE) {
            return MIN_VALUE;
        }

        if (value > MAX_VALUE) {
            return MAX_VALUE;
        }

        return value;
    }

    private void validateKeyIsNotUsedByOtherAction(int newKey, int currentKey) {
        if (newKey == currentKey) {
            return;
        }

        if (isKeyUsed(newKey)) {
            throw new IllegalArgumentException("This key is already assigned to another action.");
        }
    }

    private boolean isKeyUsed(int key) {

        return
            key == moveUp
                || key == moveDown
                || key == moveLeft
                || key == moveRight
                || key == attack
                || key == jump
                || key == dash
                || key == focus
                || key == inventory
                || key == cast
                || key == pause;
    }
}



