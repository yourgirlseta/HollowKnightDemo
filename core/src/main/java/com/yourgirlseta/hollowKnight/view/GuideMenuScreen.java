package com.yourgirlseta.hollowKnight.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.controller.GuideMenuController;
import com.yourgirlseta.hollowKnight.model.enums.ControlAction;
import com.yourgirlseta.hollowKnight.model.enums.Menu;
import com.yourgirlseta.hollowKnight.model.map.DreamParticleEffect;

public class GuideMenuScreen extends ScreenAdapter implements AppMenu {

    private final Main game;
    private final Screen previousScreen;
    private final GuideMenuController controller;
    private DreamParticleEffect dreamParticleEffect;

    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;

    private Table controlsSection;
    private Table abilitiesSection;
    private Table cheatsSection;

    private TextButton controlsTabButton;
    private TextButton abilitiesTabButton;
    private TextButton cheatsTabButton;

    public GuideMenuScreen(Main game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.controller = new GuideMenuController(
            game.getSettingsManager(),
            game.getControlsManager()
        );
    }

    @Override
    public Menu getMenu() {
        return Menu.GUIDE_MENU;
    }

    @Override
    public void show() {

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        skin = createBasicSkin();

        backgroundTexture = new Texture(game.getSettingsManager().getSettingsData().getTheme().backgroundPath);

        buildUi();
    }

    private void buildUi() {

        Stack root = new Stack();
        root.setFillParent(true);

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);

        Table contentLayer = new Table();
        contentLayer.setFillParent(true);
        contentLayer.top().padTop(40f);
        dreamParticleEffect = new DreamParticleEffect(WORLD_WIDTH, WORLD_HEIGHT, 24);
        Label title = new Label(controller.getTitleText(), skin);
        title.setFontScale(2f);
        contentLayer.add(title).colspan(3).padBottom(20f).row();


        controlsTabButton = new TextButton(controller.getControlsTabText(), skin);
        abilitiesTabButton = new TextButton(controller.getAbilitiesTabText(), skin);
        cheatsTabButton = new TextButton(controller.getCheatsTabText(), skin);

        Table tabRow = new Table();
        tabRow.add(controlsTabButton).width(280f).height(50f).pad(8f);
        tabRow.add(abilitiesTabButton).width(280f).height(50f).pad(8f);
        tabRow.add(cheatsTabButton).width(280f).height(50f).pad(8f);

        contentLayer.add(tabRow).colspan(3).padBottom(20f).row();

        controlsSection = buildControlsSection();
        abilitiesSection = buildAbilitiesSection();
        cheatsSection = buildCheatsSection();

        final ScrollPane scrollPane = new ScrollPane(controlsSection, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        contentLayer.add(scrollPane).width(1000f).height(700f).colspan(3).row();

        controlsTabButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                scrollPane.setActor(controlsSection);
            }
        });

        abilitiesTabButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                scrollPane.setActor(abilitiesSection);
            }
        });

        cheatsTabButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                scrollPane.setActor(cheatsSection);
            }
        });

        TextButton backButton = new TextButton(controller.getBackText(), skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(previousScreen);
            }
        });

        contentLayer.add(backButton).width(220f).height(50f).padTop(20f).colspan(3);

        root.add(background);
        root.add(dreamParticleEffect);
        root.add(contentLayer);
        stage.addActor(root);
    }

    private Table buildControlsSection() {

        Table table = new Table();
        table.top();
        table.defaults().pad(6f);

        ControlAction[] actions = {
            ControlAction.MOVE_UP,
            ControlAction.MOVE_DOWN,
            ControlAction.MOVE_LEFT,
            ControlAction.MOVE_RIGHT,
            ControlAction.JUMP,
            ControlAction.DASH,
            ControlAction.ATTACK,
            ControlAction.FOCUS,
            ControlAction.CAST,
            ControlAction.INVENTORY,
            ControlAction.PAUSE
        };

        for (ControlAction action : actions) {
            Label line = new Label(controller.getControlLine(action), skin);
            table.add(line).left().row();
        }

        return table;
    }

    private Table buildAbilitiesSection() {

        Table table = new Table();
        table.top();
        table.defaults().pad(10f);

        for (GuideMenuController.AbilityEntry entry : controller.getAbilityEntries()) {

            Label titleLabel = new Label(entry.title, skin);
            titleLabel.setFontScale(1.1f);

            Label descLabel = new Label(entry.description, skin);
            descLabel.setWrap(true);
            descLabel.setFontScale(0.8f);

            table.add(titleLabel).left().width(900f).row();
            table.add(descLabel).left().width(900f).padBottom(15f).row();
        }

        return table;
    }


    private Table buildCheatsSection() {

        Table table = new Table();
        table.top();
        table.defaults().pad(10f);

        for (GuideMenuController.CheatEntry entry : controller.getCheatEntries()) {

            Label titleLabel = new Label(entry.key, skin);
            titleLabel.setFontScale(1.1f);

            Label descLabel = new Label(entry.description, skin);
            descLabel.setWrap(true);
            descLabel.setFontScale(0.8f);

            table.add(titleLabel).left().width(900f).row();
            table.add(descLabel).left().width(900f).padBottom(15f).row();
        }

        return table;
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
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (dreamParticleEffect != null) dreamParticleEffect.dispose();
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
