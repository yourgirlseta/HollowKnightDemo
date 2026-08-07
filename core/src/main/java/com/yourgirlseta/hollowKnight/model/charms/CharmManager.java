package com.yourgirlseta.hollowKnight.model.charms;

import com.yourgirlseta.hollowKnight.model.enums.CharmType;

import java.util.HashSet;
import java.util.Set;

public class CharmManager {

    private static final int MAX_NOTCHES = 3;

    private final Set<CharmType> equippedCharms = new HashSet<>();
    private final Set<CharmType> unlockedCharms = new HashSet<>();

    public CharmManager() {
        for (CharmType type : CharmType.values()) {
            if (type != CharmType.VOID_HEART) {
                unlockedCharms.add(type);
            }
        }
    }

    public boolean isUnlocked(CharmType type) {
        return unlockedCharms.contains(type);
    }

    public void unlock(CharmType type) {
        unlockedCharms.add(type);
    }

    public int getMaxNotches() {
        return MAX_NOTCHES;
    }

    public boolean canEquip(CharmType type) {
        if (!isUnlocked(type)) return false;
        if (isEquipped(type)) return false;
        return getUsedNotches() + type.notchCost <= MAX_NOTCHES;
    }

    public boolean toggle(CharmType type) {
        if (!isUnlocked(type)) {
            return false;
        }
        if (isEquipped(type)) {
            equippedCharms.remove(type);
            return true;
        }
        if (canEquip(type)) {
            equippedCharms.add(type);
            return true;
        }
        return false;
    }


    public boolean isEquipped(CharmType type) {
        return equippedCharms.contains(type);
    }

    public int getUsedNotches() {
        int used = 0;
        for (CharmType type : equippedCharms) {
            used += type.notchCost;
        }
        return used;
    }

    public Set<CharmType> getEquippedCharms() {
        return equippedCharms;
    }
    public boolean hasDashmaster() {
        return isEquipped(CharmType.DASHMASTER);
    }

    public float getSoulMultiplier() {
        return isEquipped(CharmType.SOUL_CATCHER) ? 1.5f : 1f;
    }

    public float getNailDamageMultiplier() {
        return isEquipped(CharmType.UNBREAKABLE_STRENGTH) ? 1.5f : 1f;
    }

    public float getAttackCooldownMultiplier() {
        return isEquipped(CharmType.QUICK_SLASH) ? 0.6f : 1f;
    }

    public float getDashCooldownMultiplier() {
        return isEquipped(CharmType.DASHMASTER) ? 0.5f : 1f;
    }

    public float getFocusDurationMultiplier() {
        return isEquipped(CharmType.QUICK_FOCUS) ? 0.6f : 1f;
    }

    public float getKnockbackMultiplier() {
        return isEquipped(CharmType.HEAVY_BLOW) ? 3f : 1f;
    }

    public boolean hasSharpShadow() {
        return isEquipped(CharmType.SHARP_SHADOW);
    }

    public float getDashLengthMultiplier() {
        return hasSharpShadow() ? 1.2f : 1f;
    }

    public boolean hasVoidHeart() {
        return isEquipped(CharmType.VOID_HEART);
    }

    public float getSpellDamageMultiplier() {
        return hasVoidHeart() ? 1.5f : 1f;
    }
}
