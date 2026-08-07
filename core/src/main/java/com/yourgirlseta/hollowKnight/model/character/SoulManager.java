package com.yourgirlseta.hollowKnight.model.character;

public class SoulManager {

    public static final int MAX_SOUL = 99;

    private int currentSoul;

    public void addSoul(int amount) {

        currentSoul += amount;

        if (currentSoul > MAX_SOUL) {
            currentSoul = MAX_SOUL;
        }
    }

    public boolean consumeSoul(int amount) {

        if (currentSoul < amount) {
            return false;
        }

        currentSoul -= amount;

        return true;
    }

    public int getCurrentSoul() {
        return currentSoul;
    }

    public int getMaxSoul() {
        return MAX_SOUL;
    }

    public void setSoul(int soul) {

        currentSoul = Math.max(
            0,
            Math.min(MAX_SOUL, soul)
        );
    }

    public boolean hasEnoughSoul(int amount) {
        return currentSoul >= amount;
    }

    public void clearSoul() {
        currentSoul = 0;
    }

    public boolean isFull() {
        return currentSoul >= MAX_SOUL;
    }

    public int getSoul() {
        return currentSoul;
    }
}
