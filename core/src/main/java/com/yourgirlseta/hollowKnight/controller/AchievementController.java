package com.yourgirlseta.hollowKnight.controller;

import com.yourgirlseta.hollowKnight.model.achievement.AchievementManager;
import com.yourgirlseta.hollowKnight.model.enums.AchievementType;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;

public class AchievementController {

    private final SettingsManager settingsManager;
    private final AchievementManager achievementManager;

    public AchievementController(SettingsManager settingsManager, AchievementManager achievementManager) {
        this.settingsManager = settingsManager;
        this.achievementManager = achievementManager;
    }

    private boolean isFrench() {
        return settingsManager.getSettingsData().getLanguage() == Language.FRENCH;
    }

    public String getTitleText() {
        return isFrench() ? "Hauts Faits" : "Achievements";
    }

    public String getBackText() {
        return isFrench() ? "RETOUR" : "BACK";
    }

    public String getLockedText() {
        return isFrench() ? "Verrouillé" : "Locked";
    }

    public boolean isUnlocked(AchievementType type) {
        return achievementManager.isUnlocked(type);
    }

    public String getDisplayName(AchievementType type) {
        return isFrench() ? type.displayNameFr : type.displayNameEn;
    }

    public String getDescription(AchievementType type) {
        return isFrench() ? type.descriptionFr : type.descriptionEn;
    }
}
