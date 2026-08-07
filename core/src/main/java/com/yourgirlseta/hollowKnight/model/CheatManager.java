package com.yourgirlseta.hollowKnight.model;

public class CheatManager {
    private boolean emergencyHeal;

    private boolean noclip;

    private boolean godMode;

    private boolean teleportToBoss;

    private boolean fillSoul;

    public void fillSoul() {
        fillSoul = true;
    }

    public boolean shouldFillSoul() {
        return fillSoul;
    }

    public void consumeFillSoul() {
        fillSoul = false;
    }

    public boolean shouldTeleportToBoss() {
        return teleportToBoss;
    }

    public void teleportToBoss() {
        teleportToBoss = true;
    }

    public void consumeBossTeleport() {
        teleportToBoss = false;
    }

    public boolean isEmergencyHeal() {

        return emergencyHeal;
    }

    public void setEmergencyHeal(
        boolean emergencyHeal
    ) {

        this.emergencyHeal =
            emergencyHeal;
    }

    public boolean isNoclip() {

        return noclip;
    }

    public void setNoclip(
        boolean noclip
    ) {

        this.noclip = noclip;
    }

    public boolean isGodMode() {

        return godMode;
    }

    public void setGodMode(
        boolean godMode
    ) {

        this.godMode = godMode;
    }
}
