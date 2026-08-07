package com.yourgirlseta.hollowKnight.controller;

import com.yourgirlseta.hollowKnight.model.enums.CharmType;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;

public class InventoryController {

    private final SettingsManager settingsManager;

    public InventoryController(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    private boolean isFrench() {
        return settingsManager.getSettingsData().getLanguage() == Language.FRENCH;
    }

    public String getTitleText() {
        return isFrench() ? "Charmes" : "Charms";
    }

    public String getNotchLabelText(int used, int max) {
        return isFrench()
            ? "Encoches : " + used + " / " + max
            : "Notches: " + used + " / " + max;
    }

    public String getCloseHintText() {
        return isFrench() ? "Appuyez sur I pour fermer" : "Press I to close";
    }

    public String getCharmName(CharmType type) {
        return type.displayName;
    }

    public String getCharmDescription(CharmType type) {
        return isFrench() ? type.descriptionFr : type.descriptionEn;
    }

    public String getDeniedText() {
        return isFrench() ? "Encoches insuffisantes !" : "Not enough notches!";
    }
}
