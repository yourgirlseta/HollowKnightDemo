package com.yourgirlseta.hollowKnight.controller;

import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;

public class VictoryScreenController {

    private final SettingsManager settingsManager;

    public VictoryScreenController(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    private boolean isFrench() {
        return settingsManager.getSettingsData().getLanguage() == Language.FRENCH;
    }

    public String getTitleText() {
        return isFrench() ? "VICTOIRE" : "VICTORY";
    }

    public String getSubtitleText() {
        return isFrench()
            ? "Le Faux Chevalier est tombé"
            : "The False Knight has fallen";
    }

    public String getPlayTimeLabel() {
        return isFrench() ? "Temps de jeu" : "Play Time";
    }

    public String getEnemiesDefeatedLabel() {
        return isFrench() ? "Ennemis vaincus" : "Enemies Defeated";
    }

    public String getDeathsLabel() {
        return isFrench() ? "Morts" : "Deaths";
    }

    public String getRestartButtonText() {
        return isFrench() ? "Recommencer" : "Restart";
    }

    public String getBackButtonText() {
        return isFrench() ? "Retour au menu principal" : "Return to Main Menu";
    }
}

