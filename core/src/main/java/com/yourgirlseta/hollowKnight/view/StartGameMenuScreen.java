package com.yourgirlseta.hollowKnight.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.controller.MainMenuController;
import com.yourgirlseta.hollowKnight.controller.StartGameMenuController;
import com.yourgirlseta.hollowKnight.model.GameData;
import com.yourgirlseta.hollowKnight.model.SaveManager;
import com.yourgirlseta.hollowKnight.model.enums.Menu;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.yourgirlseta.hollowKnight.model.map.DreamParticleEffect;

import javax.swing.event.ChangeEvent;

public class StartGameMenuScreen extends ScreenAdapter implements AppMenu {
    private final Main game;
    private final SaveManager saveManager;
    private StartGameMenuController controller;

    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;

    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    public StartGameMenuScreen(Main game) {
        this.game = game;
        this.saveManager = game.getSaveManager();
    }

    @Override
    public Menu getMenu() {
        return Menu.START_GAME_MENU;
    }


    @Override
    public void show() {
        controller = new StartGameMenuController(game);

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        skin = createBasicSkin();

        backgroundTexture = new Texture(game.getSettingsManager().getSettingsData().getTheme().backgroundPath);

        game.getAudioManager().playMusic(game.getMenuMusic(), true);

        Stack root = new Stack();
        root.setFillParent(true);

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);

        Table contentLayer = new Table();
        contentLayer.setFillParent(true);
        contentLayer.center();

        Table table = new Table();
        table.center();

        TextButton newGameButton = new TextButton("NEW GAME", skin);
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onNewGameClicked();
            }
        });

        table.add(newGameButton).width(700).height(90).pad(10);
        table.row();

        for (
            int slot = 1;
            slot <= 4;
            slot++
        ) {

            final int slotId =
                slot;

            GameData data =
                saveManager.loadGame(
                    slotId
                );

            Texture slotTexture;

            if (
                data != null
                    &&
                    "Map2".equals(
                        data.mapName
                    )
            ) {

                slotTexture =
                    new Texture(
                        "Area save art/Area_Royal_Gardens.png"
                    );

            } else if (
                data != null
                    &&
                    "Map1".equals(
                        data.mapName
                    )
            ) {

                slotTexture =
                    new Texture(
                        "Area save art/Area_Forgotten Crossroads.png"
                    );
            } else {
                slotTexture =
                    new Texture(
                        "Area save art/Area_Shaman_Temple.png"
                    );
            }

            TextureRegionDrawable bgDrawable =
                new TextureRegionDrawable(
                    new TextureRegion(
                        slotTexture
                    )
                );

            TextButton.TextButtonStyle
                slotStyle =

                new TextButton.TextButtonStyle(
                    skin.get(
                        TextButton.TextButtonStyle.class
                    )
                );

            slotStyle.up =
                bgDrawable;

            slotStyle.down =
                bgDrawable;

            TextButton slotButton =
                new TextButton(
                    controller.getSlotText(
                        slotId
                    ),
                    slotStyle
                );

            slotButton.getLabel()
                .setWrap(true);

            slotButton.getLabel()
                .setAlignment(
                    Align.left
                );

            slotButton.addListener(
                new ChangeListener() {

                    @Override
                    public void changed(
                        ChangeEvent event,
                        Actor actor
                    ) {

                        controller.onSlotClicked(
                            slotId
                        );
                    }
                }
            );

            table.add(slotButton)
                .width(700)
                .height(90)
                .pad(10);

            table.row();
        }

        TextButton backButton = new TextButton("BACK", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onBackClicked();
            }
        });

        table.add(backButton).width(300).height(60).pad(10);

        contentLayer.add(table);
        root.add(background);
        root.add(contentLayer);

        stage.addActor(root);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

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

    }

    private Skin createBasicSkin() {
        Skin basicSkin = new Skin();

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
        checkboxOffPixmap.drawCircle(14, 14, 12);
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

        return basicSkin;
    }

}

