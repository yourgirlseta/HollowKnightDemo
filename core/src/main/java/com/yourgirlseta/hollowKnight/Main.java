 package com.yourgirlseta.hollowKnight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.yourgirlseta.hollowKnight.controller.SettingsMenuController;
import com.yourgirlseta.hollowKnight.model.CheatManager;
import com.yourgirlseta.hollowKnight.model.DatabaseManager;
import com.yourgirlseta.hollowKnight.model.GameData;
import com.yourgirlseta.hollowKnight.model.SaveManager;
import com.yourgirlseta.hollowKnight.model.achievement.AchievementManager;
import com.yourgirlseta.hollowKnight.model.achievement.AchievementRepository;
import com.yourgirlseta.hollowKnight.model.charms.CharmManager;
import com.yourgirlseta.hollowKnight.model.settingsUtils.*;
import com.yourgirlseta.hollowKnight.model.spells.HowlingWraiths;
import com.yourgirlseta.hollowKnight.model.spells.VengefulSpirit;
import com.yourgirlseta.hollowKnight.view.MainMenuScreen;
import com.yourgirlseta.hollowKnight.view.StartGameMenuScreen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;


 public class Main extends Game {
     private Cursor customCursor;
     private SettingsManager settingsManager;
     private SettingsData settingsData;
     private AudioManager audioManager;
     private Music menuMusic;
     private Music forgottonCrossRoadsMusic;
     private Music greenPathMusic;
     private Music victoryMusic;
     private Music bossRoom;
     private ControlsManager controlsManager;
     private BrightnessManager brightnessManager;
     private DatabaseManager databaseManager;
     private SaveManager saveManager;
     private CheatManager cheatManager;
     private CharmManager charmManager;
     private AchievementManager achievementManager;
     private GameData gameData;

     @Override
     public void create() {
         setupCursor();
         settingsManager = new SettingsManager();
         settingsData = settingsManager.getSettingsData();

         audioManager = new AudioManager();
         audioManager.setMusicVolume(settingsData.getMusicVolume());
         audioManager.setMusicMuted(settingsData.isMusicMuted());

         controlsManager = new ControlsManager(settingsManager.getSettingsData());
         brightnessManager = new BrightnessManager(settingsManager);

         databaseManager = new DatabaseManager();
         databaseManager.initializeDatabase();

         saveManager = new SaveManager(databaseManager);
         cheatManager = new CheatManager();
         charmManager = new CharmManager();
         AchievementRepository achievementRepository = new AchievementRepository(databaseManager);
         achievementManager = new AchievementManager(achievementRepository);
         gameData = new GameData();

         menuMusic = Gdx.audio.newMusic(Gdx.files.internal(
             "songs/game/menu/Sirvan Khosravi - Sakhteman Pezeshkan (320)-[AudioTrimmer.com].mp3"));

         forgottonCrossRoadsMusic =  Gdx.audio.newMusic(Gdx.files.internal(
             "songs/S19 Crossroads Main.wav"));

         greenPathMusic = Gdx.audio.newMusic(Gdx.files.internal(
             "songs/S5 Green Path Main.wav"
         ));

         victoryMusic = Gdx.audio.newMusic(Gdx.files.internal(
             "songs/Careless Whisper   Wham!.mp3"
         ));

         bossRoom = Gdx.audio.newMusic(Gdx.files.internal(
             "songs/4chaos-lil-curly-dota.mp3"
         ));

         setScreen(new MainMenuScreen(this));
     }

     public GameData getGameData() {
         return gameData;
     }

     public SettingsManager getSettingsManager() {
        return settingsManager;
    }

     public SettingsData getSettingsData() {
        return settingsData;
    }

     public AudioManager getAudioManager() {
        return audioManager;
    }

     public ControlsManager getControlsManager() {
        return controlsManager;
    }

     public BrightnessManager brightnessManager() {
        return brightnessManager;
    }

     public SaveManager getSaveManager() {
         return saveManager;
     }

    public Music getMenuMusic() {
        return menuMusic;
    }

    public Music getForgottonCrossRoadsMusic() {return  forgottonCrossRoadsMusic; }

    public Music getGreenPathMusic() {return  greenPathMusic; }

    public Music getVictoryMusic() {return victoryMusic; }

     public Music getBossRoomMusic() {return bossRoom;}

     public CheatManager getCheatManager() {
         return cheatManager;
     }

     public CharmManager getCharmManager() {return charmManager;}

     public AchievementManager getAchievementManager() {return achievementManager;}

     private void setupCursor() {
         Pixmap sourcePixmap = new Pixmap(Gdx.files.internal("img.png"));

         Pixmap cursorPixmap = new Pixmap(
             sourcePixmap.getWidth(),
             sourcePixmap.getHeight(),
             Pixmap.Format.RGBA8888
         );

         cursorPixmap.drawPixmap(sourcePixmap, 0, 0);

         customCursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
         Gdx.graphics.setCursor(customCursor);

         sourcePixmap.dispose();
         cursorPixmap.dispose();
     }

    @Override
    public void dispose() {
        if (menuMusic != null) {
            menuMusic.dispose();
        }

        VengefulSpirit.disposeShared();
        HowlingWraiths.disposeShared();


        super.dispose();
    }
}



