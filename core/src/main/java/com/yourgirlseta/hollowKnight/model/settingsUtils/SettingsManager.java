package com.yourgirlseta.hollowKnight.model.settingsUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.enums.Theme;

public class SettingsManager {

    private static final String PREFERENCES_NAME = "game-settings";

    private static final String MUSIC_VOLUME_KEY = "musicVolume";
    private static final String MUSIC_MUTED_KEY = "musicMuted";

    private static final String SFX_VOLUME_KEY = "sfxVolume";
    private static final String SFX_MUTED_KEY = "sfxMuted";

    private static final String BRIGHTNESS_KEY = "brightness";
    private static final String LANGUAGE_KEY = "language";

    private static final String MOVE_UP_KEY = "moveUp";
    private static final String MOVE_DOWN_KEY = "moveDown";
    private static final String MOVE_LEFT_KEY = "moveLeft";
    private static final String MOVE_RIGHT_KEY = "moveRight";
    private static final String ATTACK_KEY = "attack";
    private static final String JUMP_KEY = "jump";
    private static final String DASH_KEY = "dash";
    private static final String FOCUS_KEY = "focus";
    private static final String INVENTORY_KEY = "inventory";
    private static final String CAST_KEY = "cast";
    private static final String PAUSE_KEY = "pause";
    private static final String THEME_KEY = "theme";
    private final Preferences preferences;
    private final SettingsData settingsData;

    public SettingsManager() {
        this.preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        this.settingsData = new SettingsData();
        load();
    }

    public SettingsData getSettingsData() {
        return settingsData;
    }

    public void load() {
        settingsData.setMusicVolume(
            preferences.getFloat(MUSIC_VOLUME_KEY, settingsData.getMusicVolume())
        );
        settingsData.setMusicMuted(
            preferences.getBoolean(MUSIC_MUTED_KEY, settingsData.isMusicMuted())
        );

        settingsData.setSfxVolume(
            preferences.getFloat(SFX_VOLUME_KEY, settingsData.getSfxVolume())
        );
        settingsData.setSfxMuted(
            preferences.getBoolean(SFX_MUTED_KEY, settingsData.isSfxMuted())
        );

        settingsData.setBrightness(
            preferences.getFloat(BRIGHTNESS_KEY, settingsData.getBrightness())
        );

        String languageName = preferences.getString(
            LANGUAGE_KEY,
            settingsData.getLanguage().name()
        );
        settingsData.setLanguage(Language.valueOf(languageName));

        String themeName = preferences.getString(
            THEME_KEY,
            settingsData.getTheme().name()
        );
        settingsData.setTheme(Theme.valueOf(themeName));

        loadControlsSafely();
    }

    private void loadControlsSafely() {
        try {
            settingsData.setMoveUp(
                preferences.getInteger(MOVE_UP_KEY, settingsData.getMoveUp())
            );
            settingsData.setMoveDown(
                preferences.getInteger(MOVE_DOWN_KEY, settingsData.getMoveDown())
            );
            settingsData.setMoveLeft(
                preferences.getInteger(MOVE_LEFT_KEY, settingsData.getMoveLeft())
            );
            settingsData.setMoveRight(
                preferences.getInteger(MOVE_RIGHT_KEY, settingsData.getMoveRight())
            );
            settingsData.setAttack(
                preferences.getInteger(ATTACK_KEY, settingsData.getAttack())
            );
            settingsData.setJump(
                preferences.getInteger(JUMP_KEY, settingsData.getJump())
            );
            settingsData.setDash(
                preferences.getInteger(DASH_KEY, settingsData.getDash())
            );
            settingsData.setFocus(
                preferences.getInteger(FOCUS_KEY, settingsData.getFocus())
            );
            settingsData.setInventory(
                preferences.getInteger(INVENTORY_KEY, settingsData.getInventory())
            );
            settingsData.setCast(
                preferences.getInteger(CAST_KEY, settingsData.getCast())
            );
            settingsData.setPause(
                preferences.getInteger(PAUSE_KEY, settingsData.getPause())
            );
        } catch (IllegalArgumentException e) {
            settingsData.resetControls();
            save();
        }
    }

    public void save() {
        preferences.putFloat(MUSIC_VOLUME_KEY, settingsData.getMusicVolume());
        preferences.putBoolean(MUSIC_MUTED_KEY, settingsData.isMusicMuted());

        preferences.putFloat(SFX_VOLUME_KEY, settingsData.getSfxVolume());
        preferences.putBoolean(SFX_MUTED_KEY, settingsData.isSfxMuted());

        preferences.putFloat(BRIGHTNESS_KEY, settingsData.getBrightness());

        preferences.putString(LANGUAGE_KEY, settingsData.getLanguage().name());

        preferences.putInteger(MOVE_UP_KEY, settingsData.getMoveUp());
        preferences.putInteger(MOVE_DOWN_KEY, settingsData.getMoveDown());
        preferences.putInteger(MOVE_LEFT_KEY, settingsData.getMoveLeft());
        preferences.putInteger(MOVE_RIGHT_KEY, settingsData.getMoveRight());
        preferences.putInteger(ATTACK_KEY, settingsData.getAttack());
        preferences.putInteger(JUMP_KEY, settingsData.getJump());
        preferences.putInteger(DASH_KEY, settingsData.getDash());
        preferences.putInteger(FOCUS_KEY, settingsData.getFocus());
        preferences.putInteger(INVENTORY_KEY, settingsData.getInventory());
        preferences.putInteger(CAST_KEY, settingsData.getCast());
        preferences.putInteger(PAUSE_KEY, settingsData.getPause());
        preferences.putString(THEME_KEY, settingsData.getTheme().name());

        preferences.flush();
    }

    public void resetTheme() {
        settingsData.resetTheme();
        save();
    }

    public void resetControls() {
        settingsData.resetControls();
        save();
    }

    public void resetAudio() {
        settingsData.resetAudio();
        save();
    }

    public void resetBrightness() {
        settingsData.resetBrightness();
        save();
    }

    public void resetLanguage() {
        settingsData.resetLanguage();
        save();
    }

    public void resetAll() {
        settingsData.resetAll();
        save();
    }
}

