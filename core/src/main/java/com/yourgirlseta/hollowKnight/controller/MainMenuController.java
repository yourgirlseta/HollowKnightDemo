package com.yourgirlseta.hollowKnight.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.model.settingsUtils.AudioManager;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsData;
import com.yourgirlseta.hollowKnight.view.*;

public class MainMenuController {
    private final Main game;
    private final SettingsData settingsData;
    private final AudioManager audioManager;

    public MainMenuController(Main game, SettingsData settingsData, AudioManager audioManager) {
        this.game = game;
        this.settingsData = settingsData;
        this.audioManager = audioManager;
    }

    public void onStartGameClicked() {
        game.setScreen(new StartGameMenuScreen(game));
    }

    public void onSettingsClicked() {game.setScreen(new SettingsMenuScreen(game, game.getScreen()));}

    public void onGuideClicked() {
        game.setScreen(new GuideMenuScreen(game, game.getScreen()));
    }

    public void onAchievementsClicked() {
        game.setScreen(new AchievementMenuScreen(game, game.getScreen()));
    }

    public void onQuitClicked() {
        Gdx.app.exit();
    }
}


