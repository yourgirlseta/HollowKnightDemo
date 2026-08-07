package com.yourgirlseta.hollowKnight.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.yourgirlseta.hollowKnight.model.enums.ControlAction;
import com.yourgirlseta.hollowKnight.model.enums.Theme;
import com.yourgirlseta.hollowKnight.model.settingsUtils.*;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.view.MainMenuScreen;

import com.badlogic.gdx.Input.Keys;

public class SettingsMenuController {

    private final SettingsManager settingsManager;
    private final SettingsData settingsData;
    private final AudioManager audioManager;
    private final ControlsManager controlsManager;
    private final BrightnessManager brightnessManager;

    public SettingsMenuController(SettingsManager settingsManager, AudioManager audioManager, ControlsManager controlsManager, BrightnessManager brightnessManager) {
        this.settingsManager = settingsManager;
        this.settingsData = settingsManager.getSettingsData();
        this.audioManager = audioManager;
        this.controlsManager = controlsManager;
        this.brightnessManager = brightnessManager;

        applyInitialSettings();
    }

    private void applyInitialSettings() {
        audioManager.setMusicVolume(settingsData.getMusicVolume());
        audioManager.setMusicMuted(settingsData.isMusicMuted());

        audioManager.setSfxVolume(settingsData.getSfxVolume());
        audioManager.setSfxMuted(settingsData.isSfxMuted());
    }

    public SettingsData getSettingsData() {
        return settingsData;
    }

    public void saveSettings() {
        settingsManager.save();
    }

    public void resetControls() {
        settingsManager.resetControls();
    }

    public void onMusicVolumeChanged(float volume) {
        settingsData.setMusicVolume(volume);
        audioManager.setMusicVolume(settingsData.getMusicVolume());
    }

    public void onSfxVolumeChanged(
        float volume
    ) {

        settingsManager
            .getSettingsData()
            .setSfxVolume(volume);

        audioManager.setSfxVolume(
            volume
        );

        settingsManager.save();
    }

    public float getMusicVolume() {
        return settingsData.getMusicVolume();
    }

    public float getSfxVolume() {
        return settingsManager
            .getSettingsData()
            .getSfxVolume();
    }

    public boolean isMusicMuted() {
        return settingsData.isMusicMuted();
    }

    public boolean isSfxMuted() {

        return settingsManager
            .getSettingsData()
            .isSfxMuted();
    }

    public void onMusicMutedChanged(boolean muted) {
        settingsData.setMusicMuted(muted);
        audioManager.setMusicMuted(muted);
    }

    public void onSfxMutedChanged(
        boolean muted
    ) {

        settingsManager
            .getSettingsData()
            .setSfxMuted(muted);

        audioManager.setSfxMuted(
            muted
        );

        settingsManager.save();
    }

    public void onToggleMusicMuted() {
        settingsData.toggleMusicMuted();
        audioManager.setMusicMuted(settingsData.isMusicMuted());
    }

    public void setControlKey(ControlAction action, int keycode) {
        controlsManager.setKey(action, keycode);
        settingsManager.save();
    }

    public int getControlKey(ControlAction action) {
        return controlsManager.getKey(action);
    }

    public float getBrightness() {
        return brightnessManager.getBrightness();
    }

    public void onBrightnessChanged(float brightness) {
        brightnessManager.setBrightness(brightness);
    }

    public void onBrightnessReset() {
        brightnessManager.resetBrightness();
    }

    public Language getLanguage() {
        return settingsManager
            .getSettingsData()
            .getLanguage();
    }

    public void onLanguageChanged(
        Language language
    ) {

        settingsManager
            .getSettingsData()
            .setLanguage(language);

        settingsManager.save();
    }

    public Theme getTheme() {
        return settingsManager.getSettingsData().getTheme();
    }

    public void onThemeToggled() {
        settingsManager.getSettingsData().toggleTheme();
        settingsManager.save();
    }


}



