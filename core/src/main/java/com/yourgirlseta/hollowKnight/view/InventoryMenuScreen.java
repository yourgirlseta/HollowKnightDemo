package com.yourgirlseta.hollowKnight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.controller.InventoryController;
import com.yourgirlseta.hollowKnight.model.charms.CharmManager;
import com.yourgirlseta.hollowKnight.model.enums.CharmType;
import com.yourgirlseta.hollowKnight.model.enums.Menu;
import com.yourgirlseta.hollowKnight.model.settingsUtils.ControlsManager;

public class InventoryMenuScreen extends ScreenAdapter implements AppMenu {

    private final Main game;
    private final GameScreen previousScreen;
    private final CharmManager charmManager;
    private final ControlsManager controlsManager;
    private final InventoryController controller;
    private Texture backgroundTexture;

    private static final float WORLD_WIDTH = 1280f;
    private static final float WORLD_HEIGHT = 720f;

    private Stage stage;
    private Skin skin;

    private final Array<Texture> loadedIcons = new Array<>();
    private final ObjectMap<CharmType, Image> charmImages = new ObjectMap<>();
    private final ObjectMap<CharmType, Label> equippedLabels = new ObjectMap<>();
    private Label notchLabel;
    private Label descriptionLabel;
    private Label titleLabel;

    public InventoryMenuScreen(Main game, GameScreen previousScreen, CharmManager charmManager) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.charmManager = charmManager;
        this.controlsManager = game.getControlsManager();
        this.controller = new InventoryController(game.getSettingsManager());
    }

    @Override
    public Menu getMenu() {
        return Menu.INVENTORY_MENU;
    }

    @Override
    public void show() {
        skin = createBasicSkin();

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(game.getSettingsManager().getSettingsData().getTheme().backgroundPath);

        buildUi();
    }

    private void buildUi() {

        Stack fullScreenStack = new Stack();
        fullScreenStack.setFillParent(true);

        Image bg = new Image(backgroundTexture);

        Table root = new Table();
        root.top().pad(40f);

        titleLabel = new Label(controller.getTitleText(), skin);
        titleLabel.setFontScale(2f);
        root.add(titleLabel).colspan(4).padBottom(20f).row();

        notchLabel = new Label("", skin);
        updateNotchLabel();
        root.add(notchLabel).colspan(4).padBottom(20f).row();

        Table grid = new Table();
        grid.defaults().pad(35f);

        int column = 0;

        for (CharmType type : CharmType.values()) {

            Stack cell = buildCharmCell(type);

            grid.add(cell).size(140f, 140f);

            column++;
            if (column % 4 == 0) {
                grid.row();
            }
        }

        root.add(grid).colspan(4).padBottom(20f).row();

        descriptionLabel = new Label("", skin);
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setFontScale(0.85f);
        root.add(descriptionLabel).colspan(4).width(900f).padBottom(20f).row();

        fullScreenStack.add(bg);
        fullScreenStack.add(root);

        stage.addActor(fullScreenStack);
    }


    private Stack buildCharmCell(CharmType type) {

        Texture texture = new Texture(Gdx.files.internal(type.iconPath));
        loadedIcons.add(texture);

        Image icon = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        charmImages.put(type, icon);

        Label nameLabel = new Label(controller.getCharmName(type), skin);
        nameLabel.setFontScale(0.7f);
        nameLabel.setAlignment(Align.center);
        nameLabel.setY(-20f);

        Label equippedLabel = new Label("", skin);
        equippedLabel.setAlignment(Align.topRight);
        equippedLabels.put(type, equippedLabel);

        Stack stack = new Stack();
        stack.add(icon);

        Table overlay = new Table();
        overlay.top().right();
        overlay.add(equippedLabel).pad(4f);
        stack.add(overlay);

        Table withName = new Table();
        withName.add(stack).size(140f, 140f).row();
        withName.add(nameLabel).width(140f);

        refreshCharmVisual(type);

        Stack finalStack = new Stack();
        finalStack.add(withName);

        finalStack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleCharmClick(type);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                showDescription(type);
            }
        });

        return finalStack;
    }

    private void showDescription(CharmType type) {
        if (!charmManager.isUnlocked(type)) {
            descriptionLabel.setText("???");
        } else {
            descriptionLabel.setText(controller.getCharmDescription(type));
        }
    }


    private void handleCharmClick(CharmType type) {

        boolean success = charmManager.toggle(type);

        if (!success) {
            descriptionLabel.setText(controller.getDeniedText());
        } else {
            showDescription(type);
        }

        refreshCharmVisual(type);
        updateNotchLabel();
    }

    private void refreshCharmVisual(CharmType type) {

        boolean unlocked = charmManager.isUnlocked(type);
        boolean equipped = charmManager.isEquipped(type);

        Image icon = charmImages.get(type);

        if (!unlocked) {
            icon.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        } else {
            icon.setColor(equipped ? Color.WHITE : new Color(0.4f, 0.4f, 0.4f, 1f));
        }

        Label equippedLabel = equippedLabels.get(type);
        if (!unlocked) {
            equippedLabel.setText("?");
            equippedLabel.setColor(Color.GRAY);
        } else {
            equippedLabel.setText(equipped ? "V" : "");
            equippedLabel.setColor(Color.LIME);
        }
    }


    private void updateNotchLabel() {
        notchLabel.setText(
            controller.getNotchLabelText(charmManager.getUsedNotches(), charmManager.getMaxNotches())
        );
    }


    @Override
    public void render(float delta) {

        if (controlsManager.isInventoryJustPressed()) {
            game.setScreen(previousScreen);
            return;
        }

        ScreenUtils.clear(0.03f, 0.03f, 0.06f, 1f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        for (Texture texture : loadedIcons) {
            texture.dispose();
        }

        if (backgroundTexture != null) {
            backgroundTexture.dispose();
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
