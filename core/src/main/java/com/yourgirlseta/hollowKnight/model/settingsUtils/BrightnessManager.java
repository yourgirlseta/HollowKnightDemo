package com.yourgirlseta.hollowKnight.model.settingsUtils;

public class BrightnessManager {

    private final SettingsManager settingsManager;
    private final SettingsData settings;

    public BrightnessManager(SettingsManager settingsManager) {
        if (settingsManager == null) {
            throw new IllegalArgumentException("SettingsManager cannot be null.");
        }

        this.settingsManager = settingsManager;
        this.settings = settingsManager.getSettingsData();
    }

    public float getBrightness() {
        return settings.getBrightness();
    }

    public void setBrightness(float brightness) {
        settings.setBrightness(brightness);
        settingsManager.save();
    }

    public void increaseBrightness(float amount) {
        setBrightness(getBrightness() + amount);
    }

    public void decreaseBrightness(float amount) {
        setBrightness(getBrightness() - amount);
    }

    public void resetBrightness() {
        settingsManager.resetBrightness();
    }
}
