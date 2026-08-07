package com.yourgirlseta.hollowKnight.controller;

import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;

public class PauseMenuController {

    private final SettingsManager
        settingsManager;

    public PauseMenuController(
        SettingsManager settingsManager
    ) {

        this.settingsManager =
            settingsManager;
    }

    public Language getLanguage() {

        return settingsManager
            .getSettingsData()
            .getLanguage();
    }

    public boolean isFrench() {

        return getLanguage()
            == Language.FRENCH;
    }

    public String getPausedText() {

        return isFrench()
            ? "PAUSE"
            : "PAUSED";
    }

    public String getContinueText() {

        return isFrench()
            ? "CONTINUER"
            : "CONTINUE";
    }

    public String getCheatsText() {

        return isFrench()
            ? "CODES TRICHE"
            : "CHEAT CODES";
    }

    public String getSettingsText() {

        return isFrench()
            ? "PARAMETRES"
            : "SETTINGS";
    }

    public String getSaveExitText() {

        return isFrench()
            ? "SAUVEGARDER ET QUITTER"
            : "SAVE & EXIT";
    }
}
