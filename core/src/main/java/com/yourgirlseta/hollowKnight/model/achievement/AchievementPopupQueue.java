package com.yourgirlseta.hollowKnight.model.achievement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.enums.AchievementType;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;

public class AchievementPopupQueue implements AchievementListener {

    private static class PopupEntry {
        AchievementType type;
        float timer = 3f;
    }

    private final Array<PopupEntry> activePopups = new Array<>();
    private static final float POPUP_DURATION = 3f;
    private static final float POPUP_HEIGHT = 90f;
    private static final float POPUP_SPACING = 10f;

    private final SettingsManager settingsManager;

    public AchievementPopupQueue(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    private boolean isFrench() {
        return settingsManager.getSettingsData().getLanguage() == Language.FRENCH;
    }

    @Override
    public void onAchievementUnlocked(AchievementType type) {
        PopupEntry entry = new PopupEntry();
        entry.type = type;
        entry.timer = POPUP_DURATION;
        activePopups.add(entry);
    }

    public void update(float delta) {
        for (int i = activePopups.size - 1; i >= 0; i--) {
            PopupEntry entry = activePopups.get(i);
            entry.timer -= delta;
            if (entry.timer <= 0f) {
                activePopups.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch, BitmapFont font, float screenWidth, float screenTop) {

        float y = screenTop - 40f;
        String prefix = isFrench() ? "Haut Fait débloqué : " : "Achievement Unlocked: ";

        for (PopupEntry entry : activePopups) {

            float alpha;

            if (entry.timer > 2.5f)
                alpha = (3f - entry.timer) / 0.5f;
            else if (entry.timer < 0.5f)
                alpha = entry.timer / 0.5f;
            else
                alpha = 1f;

            font.setColor(1f, 1f, 1f, alpha);

            String name = isFrench() ? entry.type.displayNameFr : entry.type.displayNameEn;

            font.draw(
                batch,
                prefix + name,
                screenWidth - 420f,
                y
            );

            y -= POPUP_HEIGHT + POPUP_SPACING;
        }

        font.setColor(Color.WHITE);
    }
}
