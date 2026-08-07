package com.yourgirlseta.hollowKnight.model.achievement;

import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.enemy.*;
import com.yourgirlseta.hollowKnight.model.enemy.boss.FalseKnight;
import com.yourgirlseta.hollowKnight.model.enums.AchievementType;

import java.util.HashSet;
import java.util.Set;

public class AchievementManager {

    private final AchievementRepository repository;

    private final Set<AchievementType> unlockedAchievements;
    private final Array<AchievementListener> listeners = new Array<>();

    private final Set<Class<? extends Enemy>> defeatedEnemyTypes = new HashSet<>();
    private boolean tookDamageDuringFalseKnight = false;

    private static final Class<?>[] ALL_ENEMY_TYPES = {
        Crawlid.class,
        MossFly.class,
        HuskHornhead.class,
        CrystalGuardian.class,
        FalseKnight.class
    };

    public AchievementManager(AchievementRepository repository) {
        this.repository = repository;
        this.unlockedAchievements = repository.loadUnlockedAchievements();
    }

    public void addListener(AchievementListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AchievementListener listener) {
        listeners.removeValue(listener, true);
    }

    public boolean isUnlocked(AchievementType type) {
        return unlockedAchievements.contains(type);
    }

    private void unlock(AchievementType type) {
        if (unlockedAchievements.contains(type)) return;

        unlockedAchievements.add(type);

        repository.saveUnlockedAchievement(type);

        for (AchievementListener listener : listeners) {
            listener.onAchievementUnlocked(type);
        }
    }

    public void notifyEnemyDefeated(Enemy enemy) {

        defeatedEnemyTypes.add(enemy.getClass());

        boolean hasAll = true;
        for (Class<?> type : ALL_ENEMY_TYPES) {
            if (!defeatedEnemyTypes.contains(type)) {
                hasAll = false;
                break;
            }
        }

        if (hasAll) {
            unlock(AchievementType.TRUE_HUNTER);
        }

        if (enemy instanceof FalseKnight) {
            notifyFalseKnightDefeated();
        }
    }

    private void notifyFalseKnightDefeated() {
        System.out.println("Defeated so far: " + defeatedEnemyTypes.size() + " / " + ALL_ENEMY_TYPES.length);
        for (Class<?> c : defeatedEnemyTypes) {
            System.out.println(" - " + c.getSimpleName());
        }

        unlock(AchievementType.DEFEAT_FALSE_KNIGHT);
        unlock(AchievementType.COMPLETION);

        if (!tookDamageDuringFalseKnight) {
            unlock(AchievementType.UNTOUCHABLE);
        }
    }

    public void notifyPlayerTookDamageDuringFalseKnightFight() {
        tookDamageDuringFalseKnight = true;
    }

    public void notifyGameCompleted(float playTimeSeconds) {

        unlock(AchievementType.COMPLETION);

        if (playTimeSeconds <= 600f) {
            unlock(AchievementType.SPEEDRUN);
        }
    }
}
