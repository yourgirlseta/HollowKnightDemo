package com.yourgirlseta.hollowKnight.model.achievement;

import com.yourgirlseta.hollowKnight.model.enums.AchievementType;

public interface AchievementListener {
    void onAchievementUnlocked(AchievementType type);
}
