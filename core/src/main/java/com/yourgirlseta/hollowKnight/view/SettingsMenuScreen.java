package com.yourgirlseta.hollowKnight.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.controller.SettingsMenuController;
import com.yourgirlseta.hollowKnight.model.enums.ControlAction;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.map.DreamParticleEffect;
import com.yourgirlseta.hollowKnight.model.settingsUtils.*;
import com.yourgirlseta.hollowKnight.model.enums.Menu;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class SettingsMenuScreen extends ScreenAdapter implements AppMenu {
    private final Main game;
    private final SettingsData settings;
    private final AudioManager audioManager;
    private SettingsMenuController controller;
    private ControlAction waitingForAction;
    private SettingsManager settingsManager;
    private ControlsManager controlsManager;
    private BrightnessManager brightnessManager;
    private DreamParticleEffect dreamParticleEffect;
    private SelectBox<String> languageSelectBox;
    private final Screen previousScreen;

    private TextButton moveUpButton;
    private TextButton moveDownButton;
    private TextButton moveLeftButton;
    private TextButton moveRightButton;
    private TextButton attackButton;
    private TextButton jumpButton;
    private TextButton dashButton;
    private TextButton focusButton;
    private TextButton inventoryButton;
    private TextButton pauseButton;
    private TextButton castButton;
    private Image brightnessOverlay;
    private TextButton themeButton;


    private Label musicVolumeLabel;
    private Label sfxVolumeLabel;
    private Label brightnessLabel;
    private Label controlsTitleLabel;
    private Label languageLabel;

    private CheckBox musicMuteCheckBox;
    private CheckBox sfxMuteCheckBox;

    private TextButton backButton;
    private TextButton resetBrightnessButton;

    private String waitingForKey = null;

    private Label controlsErrorLabel;

    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private TextButton resetControlsButton;

    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    public SettingsMenuScreen(Main game, Screen previousScreen) {
        this.game = game;
        this.previousScreen =
            previousScreen;

        this.settingsManager = game.getSettingsManager();
        this.settings = settingsManager.getSettingsData();

        this.audioManager = game.getAudioManager();
        this.controlsManager = game.getControlsManager();
        this.brightnessManager = game.brightnessManager();

        this.controller = new SettingsMenuController(
            settingsManager,
            audioManager,
            controlsManager,
            brightnessManager
        );
    }

    private final InputAdapter controlsInputAdapter = new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {
            if (waitingForAction == null) {
                return false;
            }
            try {
                controller.setControlKey(waitingForAction, keycode);

                waitingForAction = null;
                refreshControlButtons();
            } catch (IllegalArgumentException e) {
                showControlsError(
                    controller.getLanguage() == Language.FRENCH
                        ? "CETTE TOUCHE EST DÉJÀ ASSIGNÉE."
                        : "THIS KEY IS ALREADY ASSIGNED."
                );
            }

            return true;
        }
    };

    private void refreshLanguageTexts() {

        boolean french =
            controller.getLanguage()
                == Language.FRENCH;

        musicVolumeLabel.setText(
            french
                ? "VOLUME MUSIQUE"
                : "MUSIC VOLUME"
        );

        musicMuteCheckBox.setText(
            french
                ? "COUPER MUSIQUE"
                : "MUTE MUSIC"
        );

        sfxVolumeLabel.setText(
            french
                ? "VOLUME SFX"
                : "SFX VOLUME"
        );

        sfxMuteCheckBox.setText(
            french
                ? "COUPER SFX"
                : "MUTE SFX"
        );

        controlsTitleLabel.setText(
            french
                ? "CONTROLES"
                : "CONTROLS"
        );

        resetControlsButton.setText(
            french
                ? "REINITIALISER CONTROLES"
                : "RESET CONTROLS"
        );

        brightnessLabel.setText(
            french
                ? "LUMINOSITE"
                : "BRIGHTNESS"
        );

        resetBrightnessButton.setText(
            french
                ? "REINITIALISER LUMINOSITE"
                : "RESET BRIGHTNESS"
        );

        languageLabel.setText(
            french
                ? "LANGUE"
                : "LANGUAGE"
        );

        backButton.setText(
            french
                ? "RETOUR"
                : "BACK"
        );

        themeButton.setText(
            french ? "CHANGER DE THÈME" : "TOGGLE THEME"
        );


        refreshControlButtons();
    }

    private void showControlsError(String message) {
        controlsErrorLabel.setText(message);
    }

    private TextButton createControlButton(final ControlAction action) {
        TextButton button = new TextButton("", skin);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                waitingForAction = action;
                controlsErrorLabel.setText(
                    (controller.getLanguage() == Language.FRENCH ? "Appuyez sur une touche pour : " : "Press key for: ")
                        + getLocalizedActionName(action)
                );
            }
        });

        return button;
    }

    private void updateBrightnessOverlay(float brightness) {
        float darkness = 1f - brightness;
        brightnessOverlay.getColor().a = darkness * 0.6f;
    }


    @Override
    public Menu getMenu() {
        return Menu.SETTINGS_MENU;
    }

    private String getLocalizedActionName(ControlAction action) {

        boolean french =
            controller.getLanguage()
                == Language.FRENCH;

        switch (action) {
            case MOVE_UP:
                return french ? "Haut" : "Move Up";

            case MOVE_DOWN:
                return french ? "Bas" : "Move Down";

            case MOVE_LEFT:
                return french ? "Gauche" : "Move Left";

            case MOVE_RIGHT:
                return french ? "Droite" : "Move Right";

            case ATTACK:
                return french ? "Attaque" : "Attack";

            case JUMP:
                return french ? "Saut" : "Jump";

            case DASH:
                return french ? "Esquive" : "Dash";

            case FOCUS:
                return french ? "Concentration" : "Focus";

            case INVENTORY:
                return french ? "Inventaire" : "Inventory";

            case CAST:
                return french ? "Sort" : "Cast";

            case PAUSE:
                return french ? "Pause" : "Pause";

            default:
                throw new IllegalArgumentException("Unknown control action.");
        }
    }

    private void refreshControlButtons() {
        moveUpButton.setText(getLocalizedActionName(ControlAction.MOVE_UP) + ": " + getLocalizedKeyName(settings.getMoveUp()));
        moveDownButton.setText(getLocalizedActionName(ControlAction.MOVE_DOWN) + ": " + getLocalizedKeyName(settings.getMoveDown()));
        moveLeftButton.setText(getLocalizedActionName(ControlAction.MOVE_LEFT) + ": " + getLocalizedKeyName(settings.getMoveLeft()));
        moveRightButton.setText(getLocalizedActionName(ControlAction.MOVE_RIGHT) + ": " + getLocalizedKeyName(settings.getMoveRight()));

        attackButton.setText(getLocalizedActionName(ControlAction.ATTACK) + ": " + getLocalizedKeyName(settings.getAttack()));
        jumpButton.setText(getLocalizedActionName(ControlAction.JUMP) + ": " + getLocalizedKeyName(settings.getJump()));
        dashButton.setText(getLocalizedActionName(ControlAction.DASH) + ": " + getLocalizedKeyName(settings.getDash()));
        focusButton.setText(getLocalizedActionName(ControlAction.FOCUS) + ": " + getLocalizedKeyName(settings.getFocus()));
        inventoryButton.setText(getLocalizedActionName(ControlAction.INVENTORY) + ": " + getLocalizedKeyName(settings.getInventory()));
        pauseButton.setText(getLocalizedActionName(ControlAction.PAUSE) + ": " + getLocalizedKeyName(settings.getPause()));
        castButton.setText(getLocalizedActionName(ControlAction.CAST) + ": " + getLocalizedKeyName(settings.getCast()));
    }

    private String getLocalizedKeyName(int keycode) {

        String key =
            Input.Keys.toString(keycode);

        if (
            controller.getLanguage()
                == Language.FRENCH
        ) {

            switch (key.toUpperCase()) {

                case "UP":
                    return "HAUT";

                case "DOWN":
                    return "BAS";

                case "LEFT":
                    return "GAUCHE";

                case "RIGHT":
                    return "DROITE";

                case "SPACE":
                    return "ESPACE";

                case "ESCAPE":
                    return "ECHAP";

                default:
                    return key;
            }
        }

        return key;
    }

    @Override
    public void show() {
        boolean french = controller.getLanguage() == Language.FRENCH;
        String musicVolumeText =
            french
                ? "VOLUME MUSIQUE"
                : "MUSIC VOLUME";

        String muteMusicText =
            french
                ? "COUPER LA MUSIQUE"
                : "MUTE MUSIC";

        String sfxVolumeText =
            french
                ? "VOLUME SFX"
                : "SFX VOLUME";

        String muteSfxText =
            french
                ? "COUPER LES SFX"
                : "MUTE SFX";

        String controlsText =
            french
                ? "CONTRÔLES"
                : "CONTROLS";

        String brightnessText =
            french
                ? "LUMINOSITÉ"
                : "BRIGHTNESS";

        String resetBrightnessText =
            french
                ? "RÉINITIALISER LA LUMINOSITÉ"
                : "RESET BRIGHTNESS";

        String resetControlsText =
            french
                ? "RÉINITIALISER LES CONTRÔLES"
                : "RESET CONTROLS";

        String languageText =
            french
                ? "LANGUE"
                : "LANGUAGE";

        String backText =
            french
                ? "RETOUR"
                : "BACK";

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(controlsInputAdapter);
        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);

        game.getAudioManager().playMusic(game.getMenuMusic(), true);

        skin = createBasicSkin();

        backgroundTexture = new Texture(game.getSettingsManager().getSettingsData().getTheme().backgroundPath);

        Stack root = new Stack();
        root.setFillParent(true);

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);

        Table contentLayer = new Table();
        contentLayer.setFillParent(true);

        Table table = new Table();

        dreamParticleEffect = new DreamParticleEffect(WORLD_WIDTH, WORLD_HEIGHT, 24);

        themeButton = new TextButton(
            controller.getLanguage() == Language.FRENCH ? "CHANGER DE THÈME" : "TOGGLE THEME",
            skin
        );

        themeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onThemeToggled();

                backgroundTexture.dispose();
                backgroundTexture = new Texture(controller.getTheme().backgroundPath);
                background.setDrawable(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
            }
        });

        table.add(themeButton).colspan(2).padTop(15f).width(320f).height(50f).row();


        controlsErrorLabel = new Label("", skin);
        controlsErrorLabel.setColor(Color.RED);

        musicVolumeLabel = new Label(musicVolumeText, skin);

        Slider musicVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        musicVolumeSlider.setValue(controller.getMusicVolume());

        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onMusicVolumeChanged(musicVolumeSlider.getValue());
            }
        });

        table.add(musicVolumeLabel).pad(10);
        table.add(musicVolumeSlider).width(300).pad(10);
        table.row();

        musicMuteCheckBox = new CheckBox(muteMusicText, skin);
        musicMuteCheckBox.setChecked(controller.isMusicMuted());

        musicMuteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onMusicMutedChanged(musicMuteCheckBox.isChecked());
            }
        });

        table.add(musicMuteCheckBox).left().padBottom(15f);
        table.row();

        sfxVolumeLabel = new Label(sfxVolumeText, skin);
        Slider sfxVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);

        sfxVolumeSlider.setValue(controller.getSfxVolume());

        sfxVolumeSlider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    controller.onSfxVolumeChanged(sfxVolumeSlider.getValue());
                }
            }
        );

        table.add(sfxVolumeLabel).pad(10);
        table.add(sfxVolumeSlider).width(300).pad(10);
        table.row();

       sfxMuteCheckBox = new CheckBox(muteSfxText, skin);

        sfxMuteCheckBox.setChecked(controller.isSfxMuted());

        sfxMuteCheckBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    controller.onSfxMutedChanged(sfxMuteCheckBox.isChecked());
                }
            }
        );

        table.add(sfxMuteCheckBox)
            .left()
            .padBottom(15f);

        table.row();

        moveUpButton = createControlButton(ControlAction.MOVE_UP);
        moveDownButton = createControlButton(ControlAction.MOVE_DOWN);
        moveLeftButton = createControlButton(ControlAction.MOVE_LEFT);
        moveRightButton = createControlButton(ControlAction.MOVE_RIGHT);
        attackButton = createControlButton(ControlAction.ATTACK);
        jumpButton = createControlButton(ControlAction.JUMP);
        dashButton = createControlButton(ControlAction.DASH);
        focusButton = createControlButton(ControlAction.FOCUS);
        inventoryButton = createControlButton(ControlAction.INVENTORY);
        pauseButton =createControlButton(ControlAction.PAUSE);
        castButton =createControlButton(ControlAction.CAST);


        refreshControlButtons();

        controlsTitleLabel =
            new Label(
                controlsText,
                skin
            );

        table.add(
                controlsTitleLabel
            )
            .colspan(2)
            .padTop(25)
            .row();

        table.add(controlsErrorLabel).colspan(2).padTop(10).row();

        table.add(moveUpButton).colspan(2).width(350).pad(6).row();
        table.add(moveDownButton).colspan(2).width(350).pad(6).row();
        table.add(moveLeftButton).colspan(2).width(350).pad(6).row();
        table.add(moveRightButton).colspan(2).width(350).pad(6).row();
        table.add(attackButton).colspan(2).width(350).pad(6).row();
        table.add(jumpButton).colspan(2).width(350).pad(6).row();
        table.add(dashButton).colspan(2).width(350).pad(6).row();
        table.add(focusButton).colspan(2).width(350).pad(6).row();
        table.add(inventoryButton).colspan(2).width(350).pad(6).row();
        table.add(pauseButton).colspan(2).width(350).pad(6).row();
        table.add(castButton).colspan(2).width(350).pad(6).row();

        resetControlsButton = new TextButton(resetControlsText, skin);

        resetControlsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.resetControls();
                refreshControlButtons();

                if (controlsErrorLabel != null) {
                    controlsErrorLabel.setText("");
                }
            }
        });

        table.add(resetControlsButton).colspan(2).padTop(15).row();


        brightnessLabel = new Label(brightnessText, skin);

        Slider brightnessSlider = new Slider(0f, 1f, 0.01f, false, skin);
        brightnessSlider.setValue(controller.getBrightness());

        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float brightness = brightnessSlider.getValue();
                controller.onBrightnessChanged(brightness);
                updateBrightnessOverlay(brightness);
            }
        });

        table.add(brightnessLabel).pad(10);
        table.add(brightnessSlider).width(300).pad(10);
        table.row();

        resetBrightnessButton = new TextButton(resetBrightnessText, skin);

        resetBrightnessButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onBrightnessReset();
                brightnessSlider.setValue(controller.getBrightness());
                updateBrightnessOverlay(controller.getBrightness());
            }
        });

        table.add(resetBrightnessButton).colspan(2).padBottom(15f);
        table.row();

        languageLabel =
            new Label(languageText, skin);

        languageSelectBox =
            new SelectBox<>(skin);

        languageSelectBox.setItems(
            "English",
            "French"
        );

        if (
            controller.getLanguage()
                == Language.FRENCH
        ) {

            languageSelectBox.setSelected(
                "French"
            );

        } else {

            languageSelectBox.setSelected(
                "English"
            );
        }

        languageSelectBox.addListener(
            new ChangeListener() {

                @Override
                public void changed(
                    ChangeEvent event,
                    Actor actor
                ) {

                    String selected =
                        languageSelectBox
                            .getSelected();

                    if (
                        selected.equals("French")
                    ) {

                        controller.onLanguageChanged(
                            Language.FRENCH
                        );

                    } else {

                        controller.onLanguageChanged(
                            Language.ENGLISH
                        );
                    }

                    refreshLanguageTexts();

                    if (previousScreen instanceof GameScreen) {

                        GameScreen gameScreen =
                            (GameScreen) previousScreen;

                        if (
                            gameScreen.getFirstMap()
                                .getZote() != null
                        ) {

                            gameScreen
                                .getFirstMap()
                                .getZote()
                                .getDialogueManager()
                                .reloadLanguage();
                        }
                    }
                }
            }
        );

        table.add(languageLabel).pad(10);

        table.add(languageSelectBox)
            .width(300)
            .pad(10);

        table.row();

        backButton = new TextButton(backText, skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(
                    previousScreen
                );

                if (previousScreen instanceof GameScreen) {

                    game.getAudioManager().playMusic(
                        game.getForgottonCrossRoadsMusic(),
                        true
                    );
                }
            }
        });

        table.add(backButton).colspan(2).padTop(20f).width(220f).height(50f);
        table.row();



        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, true);

        contentLayer.add(scrollPane)
            .width(900f)
            .height(600f)
            .center();

        brightnessOverlay = new Image(skin.newDrawable("white", Color.BLACK));
        brightnessOverlay.setBounds(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        brightnessOverlay.setTouchable(Touchable.disabled);
        brightnessOverlay.getColor().a = 0f;

        root.add(background);
        root.add(contentLayer);
        root.add(brightnessOverlay);
        root.add(dreamParticleEffect);

        stage.addActor(root);
        updateBrightnessOverlay(controller.getBrightness());
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

        if (dreamParticleEffect != null) {
            dreamParticleEffect.dispose();
        }

    }

    private Skin createBasicSkin() {
        Skin basicSkin = new Skin();

        // Hollow Knight uses elegant serif fonts. TrajanPro is a great fit.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("Inventory & UI/TrajanPro-Regular.ttf")
        );

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 28;
        // Pale silver/white text
        parameter.color = new Color(0.9f, 0.9f, 0.95f, 1f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        basicSkin.add("default-font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = new Color(0.9f, 0.9f, 0.95f, 1f);
        basicSkin.add("default", labelStyle);

        Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fill();
        Texture whiteTexture = new Texture(whitePixmap);
        whitePixmap.dispose();
        basicSkin.add("white", whiteTexture);

        // --- SLIDER (Thin elegant track, double-circle knob) ---
        Pixmap sliderBackgroundPixmap = new Pixmap(300, 8, Pixmap.Format.RGBA8888);
        sliderBackgroundPixmap.setColor(new Color(0.2f, 0.2f, 0.25f, 0.6f));
        sliderBackgroundPixmap.drawLine(0, 3, 300, 3); // Thin line instead of block
        sliderBackgroundPixmap.drawLine(0, 4, 300, 4);
        Texture sliderBackgroundTexture = new Texture(sliderBackgroundPixmap);
        sliderBackgroundPixmap.dispose();

        Pixmap sliderFilledPixmap = new Pixmap(300, 8, Pixmap.Format.RGBA8888);
        sliderFilledPixmap.setColor(new Color(0.85f, 0.85f, 0.9f, 0.9f));
        sliderFilledPixmap.drawLine(0, 3, 300, 3);
        sliderFilledPixmap.drawLine(0, 4, 300, 4);
        Texture sliderFilledTexture = new Texture(sliderFilledPixmap);
        sliderFilledPixmap.dispose();

        Pixmap knobPixmap = new Pixmap(24, 24, Pixmap.Format.RGBA8888);
        knobPixmap.setColor(new Color(0.85f, 0.85f, 0.9f, 1f));
        knobPixmap.fillCircle(12, 12, 6);  // Solid inner dot
        knobPixmap.drawCircle(12, 12, 10); // Elegant outer ring
        Texture knobTexture = new Texture(knobPixmap);
        knobPixmap.dispose();

        basicSkin.add("slider-background-texture", sliderBackgroundTexture);
        basicSkin.add("slider-filled-texture", sliderFilledTexture);
        basicSkin.add("slider-knob-texture", knobTexture);

        TextureRegionDrawable sliderBackgroundDrawable = new TextureRegionDrawable(sliderBackgroundTexture);
        TextureRegionDrawable sliderFilledDrawable = new TextureRegionDrawable(sliderFilledTexture);
        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(knobTexture);

        sliderBackgroundDrawable.setMinHeight(8);
        sliderFilledDrawable.setMinHeight(8);
        knobDrawable.setMinWidth(24);
        knobDrawable.setMinHeight(24);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = sliderBackgroundDrawable;
        sliderStyle.knobBefore = sliderFilledDrawable;
        sliderStyle.knob = knobDrawable;

        basicSkin.add("default", sliderStyle);
        basicSkin.add("default-horizontal", sliderStyle);

        // --- CHECKBOX (Elegant circular charm-style toggles) ---
        Pixmap checkboxOffPixmap = new Pixmap(28, 28, Pixmap.Format.RGBA8888);
        checkboxOffPixmap.setColor(new Color(0.05f, 0.05f, 0.08f, 0.8f));
        checkboxOffPixmap.fillCircle(14, 14, 12);
        checkboxOffPixmap.setColor(new Color(0.4f, 0.4f, 0.45f, 0.8f));
        checkboxOffPixmap.drawCircle(14, 14, 12); // Subtle outer ring
        Texture checkboxOffTexture = new Texture(checkboxOffPixmap);
        checkboxOffPixmap.dispose();

        Pixmap checkboxOnPixmap = new Pixmap(28, 28, Pixmap.Format.RGBA8888);
        checkboxOnPixmap.setColor(new Color(0.05f, 0.05f, 0.08f, 0.8f));
        checkboxOnPixmap.fillCircle(14, 14, 12);
        checkboxOnPixmap.setColor(new Color(0.9f, 0.9f, 0.95f, 1f));
        checkboxOnPixmap.drawCircle(14, 14, 12); // Bright outer ring
        checkboxOnPixmap.fillCircle(14, 14, 6);  // Glowing central dot (Soul/Light feel)
        Texture checkboxOnTexture = new Texture(checkboxOnPixmap);
        checkboxOnPixmap.dispose();

        basicSkin.add("checkbox-off-texture", checkboxOffTexture);
        basicSkin.add("checkbox-on-texture", checkboxOnTexture);

        TextureRegionDrawable checkboxOffDrawable = new TextureRegionDrawable(checkboxOffTexture);
        TextureRegionDrawable checkboxOnDrawable = new TextureRegionDrawable(checkboxOnTexture);

        checkboxOffDrawable.setMinWidth(28);
        checkboxOffDrawable.setMinHeight(28);
        checkboxOnDrawable.setMinWidth(28);
        checkboxOnDrawable.setMinHeight(28);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = new Color(0.85f, 0.85f, 0.9f, 1f);
        checkBoxStyle.checkboxOff = checkboxOffDrawable;
        checkBoxStyle.checkboxOn = checkboxOnDrawable;

        basicSkin.add("default", checkBoxStyle);

        // --- SCROLLPANE ---
        Pixmap scrollBarPixmap = new Pixmap(4, 8, Pixmap.Format.RGBA8888);
        scrollBarPixmap.setColor(new Color(0.1f, 0.1f, 0.15f, 0.5f));
        scrollBarPixmap.fill();
        Texture scrollBarTexture = new Texture(scrollBarPixmap);
        scrollBarPixmap.dispose();

        Pixmap scrollKnobPixmap = new Pixmap(4, 40, Pixmap.Format.RGBA8888);
        scrollKnobPixmap.setColor(new Color(0.6f, 0.6f, 0.65f, 0.8f)); // Thin silver vertical line
        scrollKnobPixmap.fill();
        Texture scrollKnobTexture = new Texture(scrollKnobPixmap);
        scrollKnobPixmap.dispose();

        TextureRegionDrawable scrollBarDrawable = new TextureRegionDrawable(scrollBarTexture);
        TextureRegionDrawable scrollKnobDrawable = new TextureRegionDrawable(scrollKnobTexture);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = null;
        scrollPaneStyle.vScroll = scrollBarDrawable;
        scrollPaneStyle.vScrollKnob = scrollKnobDrawable;

        basicSkin.add("default", scrollPaneStyle);

        // --- TEXT BUTTON (Void background, tapered elegant lines instead of a box) ---
        // UP STATE
        Pixmap buttonPixmap = new Pixmap(260, 50, Pixmap.Format.RGBA8888);
        buttonPixmap.setColor(new Color(0.02f, 0.02f, 0.03f, 0.7f)); // Very dark, mostly transparent void
        buttonPixmap.fill();
        buttonPixmap.setColor(new Color(0.7f, 0.7f, 0.75f, 0.8f)); // Pale silver
        // Draw top and bottom framing lines (inset from the edges to look elegant)
        buttonPixmap.drawLine(20, 0, 240, 0);
        buttonPixmap.drawLine(20, 1, 240, 1);
        buttonPixmap.drawLine(20, 48, 240, 48);
        buttonPixmap.drawLine(20, 49, 240, 49);
        Texture buttonTexture = new Texture(buttonPixmap);
        buttonPixmap.dispose();

        // DOWN STATE (Brighter lines, slightly lighter void background on click)
        Pixmap buttonDownPixmap = new Pixmap(260, 50, Pixmap.Format.RGBA8888);
        buttonDownPixmap.setColor(new Color(0.1f, 0.1f, 0.15f, 0.8f));
        buttonDownPixmap.fill();
        buttonDownPixmap.setColor(new Color(1f, 1f, 1f, 1f)); // Pure white/bright silver flash
        buttonDownPixmap.drawLine(10, 0, 250, 0); // Lines extend outward when clicked
        buttonDownPixmap.drawLine(10, 1, 250, 1);
        buttonDownPixmap.drawLine(10, 48, 250, 48);
        buttonDownPixmap.drawLine(10, 49, 250, 49);
        Texture buttonDownTexture = new Texture(buttonDownPixmap);
        buttonDownPixmap.dispose();

        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonTexture);
        TextureRegionDrawable buttonDownDrawable = new TextureRegionDrawable(buttonDownTexture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = new Color(0.85f, 0.85f, 0.9f, 1f);
        textButtonStyle.downFontColor = Color.WHITE;
        textButtonStyle.up = buttonDrawable;
        textButtonStyle.down = buttonDownDrawable;

        basicSkin.add("default", textButtonStyle);

        // --- LIST (For SelectBox dropdown items) ---
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = new Color(0.7f, 0.7f, 0.75f, 1f);

        // Selection background for list items (matching the elegant button style)
        Pixmap selectionPixmap = new Pixmap(260, 40, Pixmap.Format.RGBA8888);
        selectionPixmap.setColor(new Color(0.1f, 0.1f, 0.15f, 0.8f));
        selectionPixmap.fill();
        selectionPixmap.setColor(new Color(0.7f, 0.7f, 0.75f, 0.8f)); // Pale silver lines
        selectionPixmap.drawLine(20, 0, 240, 0);
        selectionPixmap.drawLine(20, 39, 240, 39);
        Texture selectionTexture = new Texture(selectionPixmap);
        selectionPixmap.dispose();

        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(selectionTexture);
        listStyle.selection = selectionDrawable;

        // Background for the list container
        listStyle.background = new TextureRegionDrawable(buttonTexture);

        basicSkin.add("default", listStyle);

        // --- SELECT BOX ---
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = font;
        selectBoxStyle.fontColor = new Color(0.85f, 0.85f, 0.9f, 1f);
        // Reusing the elegant TextButton background for the closed SelectBox
        selectBoxStyle.background = buttonDrawable;
        selectBoxStyle.scrollStyle = scrollPaneStyle; // Using the ScrollPane style you already created
        selectBoxStyle.listStyle = listStyle;

        basicSkin.add("default", selectBoxStyle);


        return basicSkin;
    }

}
