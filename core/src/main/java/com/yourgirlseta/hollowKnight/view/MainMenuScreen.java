package com.yourgirlseta.hollowKnight.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.controller.MainMenuController;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.enums.Menu;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.yourgirlseta.hollowKnight.model.map.DreamParticleEffect;
import com.yourgirlseta.hollowKnight.model.settingsUtils.AudioManager;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsData;


public class MainMenuScreen extends ScreenAdapter implements AppMenu {
    private final Main game;
    private final SettingsData settingsData;
    private final AudioManager audioManager;
    private MainMenuController controller;
    private DreamParticleEffect dreamParticleEffect;

    private Stage stage;
    private Skin skin;

    private Texture backgroundTexture;
    private Texture leftBottomTexture;
    private Texture logoTexture;

    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    public MainMenuScreen(Main game) {
        this.game = game;
        this.settingsData = game.getSettingsData();
        this.audioManager = game.getAudioManager();
    }

    @Override
    public Menu getMenu() {
        return Menu.MAIN_MENU;
    }

    private Texture getBackgroundForLanguage() {

        Language language =
            game.getSettingsManager()
                .getSettingsData()
                .getLanguage();

        switch (language) {

            case FRENCH:

                return new Texture(
                    "Menu/vheart_title_french.png"
                );

            default:

                return new Texture(
                    "Menu/vheart_title.png"
                );
        }
    }

    private String getText(String key) {

        Language lang =
            settingsData.getLanguage();

        switch (lang) {

            case FRENCH:

                switch (key) {

                    case "start":
                        return "Commencer";

                    case "settings":
                        return "Paramètres";

                    case "guide":
                        return "Guide";

                    case "achievements":
                        return "Succès";

                    case "quit":
                        return "Quitter";

                    default:
                        return key;
                }

            default:

                switch (key) {

                    case "start":
                        return "Start Game";

                    case "settings":
                        return "Settings";

                    case "guide":
                        return "Guide";

                    case "achievements":
                        return "Achievements";

                    case "quit":
                        return "Quit Game";

                    default:
                        return key;
                }
        }
    }

    @Override
    public void show() {
        controller = new MainMenuController(game, settingsData, audioManager);

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        game.getAudioManager().playMusic(game.getMenuMusic(), true);

        Gdx.input.setInputProcessor(stage);

        skin = createBasicSkin();

        backgroundTexture = new Texture(game.getSettingsManager().getSettingsData().getTheme().backgroundPath);
        leftBottomTexture = new Texture("Menu/Hidden_Dreams_Logo.png");
        logoTexture = getBackgroundForLanguage();

        Stack root = new Stack();
        root.setFillParent(true);

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);

        Table contentLayer = new Table();
        contentLayer.setFillParent(true);

        Table leftBottomLayer = new Table();
        leftBottomLayer.setFillParent(true);
        leftBottomLayer.bottom().left();

        Image leftBottomImage = new Image(leftBottomTexture);
        leftBottomLayer.add(leftBottomImage).padLeft(10f).padBottom(10f);

        dreamParticleEffect = new DreamParticleEffect(WORLD_WIDTH, WORLD_HEIGHT, 24);

        Table menuTable = new Table();
        menuTable.left();

        Image logo = new Image(logoTexture);

        TextButton startButton =
            new TextButton(
                getText("start"),
                skin
            );

        TextButton settingsButton =
            new TextButton(
                getText("settings"),
                skin
            );

        TextButton guideButton =
            new TextButton(
                getText("guide"),
                skin
            );

        TextButton achievementsButton =
            new TextButton(
                getText("achievements"),
                skin
            );

        TextButton quitButton =
            new TextButton(
                getText("quit"),
                skin
            );

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onStartGameClicked();
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onSettingsClicked();
            }
        });

        guideButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onGuideClicked();
            }
        });

        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onAchievementsClicked();
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onQuitClicked();
            }
        });

        menuTable.add(logo).width(420f).height(180f).padBottom(30f).row();
        menuTable.add(startButton).width(260f).height(50f).padBottom(10f).row();
        menuTable.add(settingsButton).width(260f).height(50f).padBottom(10f).row();
        menuTable.add(guideButton).width(260f).height(50f).padBottom(10f).row();
        menuTable.add(achievementsButton).width(260f).height(50f).padBottom(10f).row();
        menuTable.add(quitButton).width(260f).height(50f).padTop(12f).row();

        contentLayer.left().center();
        contentLayer.add(menuTable).padLeft(90f);

        root.add(background);
        root.add(dreamParticleEffect);
        root.add(leftBottomLayer);
        root.add(contentLayer);

        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.03f, 0.03f, 0.06f, 1f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }

        if (skin != null) {
            skin.dispose();
        }

        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }

        if (leftBottomTexture != null) {
            leftBottomTexture.dispose();
        }

        if (logoTexture != null) {
            logoTexture.dispose();
        }

        if (dreamParticleEffect != null) {
            dreamParticleEffect.dispose();
        }
    }

    private Skin createBasicSkin() {
        Skin basicSkin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("assets/Inventory & UI/TrajanPro-Regular.ttf")
        );

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 32;
        parameter.color = Color.WHITE;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        basicSkin.add("default", font);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.GOLD;
        buttonStyle.downFontColor = Color.LIGHT_GRAY;

        basicSkin.add("default", buttonStyle);

        return basicSkin;
    }

}

