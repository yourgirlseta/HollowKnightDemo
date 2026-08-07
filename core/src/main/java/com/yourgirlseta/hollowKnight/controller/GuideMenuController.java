package com.yourgirlseta.hollowKnight.controller;

import com.badlogic.gdx.Input;
import com.yourgirlseta.hollowKnight.model.enums.ControlAction;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.settingsUtils.ControlsManager;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;

public class GuideMenuController {

    private final SettingsManager settingsManager;
    private final ControlsManager controlsManager;

    public GuideMenuController(SettingsManager settingsManager, ControlsManager controlsManager) {
        this.settingsManager = settingsManager;
        this.controlsManager = controlsManager;
    }

    public Language getLanguage() {
        return settingsManager.getSettingsData().getLanguage();
    }

    private boolean isFrench() {
        return getLanguage() == Language.FRENCH;
    }

    public String getTitleText() {
        return isFrench() ? "GUIDE" : "GUIDE";
    }

    public String getControlsTabText() {
        return isFrench() ? "CONTRÔLES" : "CONTROLS";
    }

    public String getAbilitiesTabText() {
        return isFrench() ? "CAPACITÉS" : "ABILITIES";
    }

    public String getCheatsTabText() {
        return isFrench() ? "CODES DE TRICHE" : "CHEAT CODES";
    }

    public String getBackText() {
        return isFrench() ? "RETOUR" : "BACK";
    }

    public String getLocalizedKeyName(int keycode) {

        String key = Input.Keys.toString(keycode);

        if (!isFrench()) {
            return key;
        }

        switch (key.toUpperCase()) {
            case "UP": return "HAUT";
            case "DOWN": return "BAS";
            case "LEFT": return "GAUCHE";
            case "RIGHT": return "DROITE";
            case "SPACE": return "ESPACE";
            case "ESCAPE": return "ECHAP";
            default: return key;
        }
    }

    public String getControlLine(ControlAction action) {

        String label;

        switch (action) {
            case MOVE_UP:
                label = isFrench() ? "Haut" : "Move Up";
                break;
            case MOVE_DOWN:
                label = isFrench() ? "Bas" : "Move Down";
                break;
            case MOVE_LEFT:
                label = isFrench() ? "Gauche" : "Move Left";
                break;
            case MOVE_RIGHT:
                label = isFrench() ? "Droite" : "Move Right";
                break;
            case JUMP:
                label = isFrench() ? "Saut" : "Jump";
                break;
            case DASH:
                label = isFrench() ? "Ruée" : "Dash";
                break;
            case ATTACK:
                label = isFrench() ? "Attaque (Nail)" : "Attack (Nail)";
                break;
            case FOCUS:
                label = isFrench() ? "Concentration" : "Focus";
                break;
            case CAST:
                label = isFrench() ? "Sort" : "Cast";
                break;
            case INVENTORY:
                label = isFrench() ? "Inventaire" : "Inventory";
                break;
            case PAUSE:
                label = isFrench() ? "Pause" : "Pause";
                break;
            default:
                label = action.name();
        }

        int keycode = controlsManager.getKey(action);
        return label + ": " + getLocalizedKeyName(keycode);
    }

    public static class AbilityEntry {
        public final String title;
        public final String description;

        public AbilityEntry(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    public java.util.List<AbilityEntry> getAbilityEntries() {

        java.util.List<AbilityEntry> list = new java.util.ArrayList<>();

        if (isFrench()) {
            list.add(new AbilityEntry(
                "Barre de santé",
                "Le Chevalier possède 5 masques de vie. Chaque coup subi brise un masque ; à zéro masque, le Chevalier meurt et réapparaît au dernier point de sauvegarde."
            ));
            list.add(new AbilityEntry(
                "Âme (Soul)",
                "Frapper un ennemi avec le Nail remplit la jauge d'Âme. L'Âme accumulée permet de se soigner (Focus) ou de lancer des sorts."
            ));
            list.add(new AbilityEntry(
                "Dash",
                "Permet une ruée rapide dans la direction actuelle. Peut être réutilisé après avoir touché le sol, ou plus tôt avec le charme Dashmaster."
            ));
            list.add(new AbilityEntry(
                "Double Saut",
                "Un second saut est disponible en l'air après le premier saut ou un mur."
            ));
            list.add(new AbilityEntry(
                "Sorts (Spells)",
                "Vengeful Spirit et Howling Wraiths consomment de l'Âme pour infliger des dégâts à distance."
            ));
        } else {
            list.add(new AbilityEntry(
                "Health Bar",
                "The Knight has 5 masks of health. Each hit taken breaks one mask; at zero masks, the Knight dies and respawns at the last bench."
            ));
            list.add(new AbilityEntry(
                "Soul",
                "Striking an enemy with the Nail fills the Soul gauge. Accumulated Soul can be spent to heal (Focus) or cast spells."
            ));
            list.add(new AbilityEntry(
                "Dash",
                "Performs a quick dash in the current facing direction. Refreshes on touching the ground, or sooner with the Dashmaster charm."
            ));
            list.add(new AbilityEntry(
                "Double Jump",
                "A second jump becomes available in midair after the first jump or a wall jump."
            ));
            list.add(new AbilityEntry(
                "Spells",
                "Vengeful Spirit and Howling Wraiths consume Soul to deal ranged damage."
            ));
        }

        return list;
    }

    public static class CheatEntry {
        public final String key;
        public final String description;

        public CheatEntry(String key, String description) {
            this.key = key;
            this.description = description;
        }
    }

    public java.util.List<CheatEntry> getCheatEntries() {

        java.util.List<CheatEntry> list = new java.util.ArrayList<>();

        if (isFrench()) {
            list.add(new CheatEntry("Téléportation Boss", "Téléporte le joueur devant le boss."));
            list.add(new CheatEntry("Vol Libre (Noclip)", "Désactive les collisions, permettant de traverser les murs."));
            list.add(new CheatEntry("Soin d'Urgence", "Accorde un masque de vie automatique en cas de mort imminente."));
            list.add(new CheatEntry("Remplir l'Âme", "Remplit instantanément la jauge d'Âme."));
            list.add(new CheatEntry("Mode Divin", "Rend le Chevalier invincible."));
            list.add(new CheatEntry("Tuer Tout", "Élimine instantanément tous les ennemis de la zone."));
        } else {
            list.add(new CheatEntry("Boss Teleport", "Teleports the player directly to the boss."));
            list.add(new CheatEntry("Noclip", "Disables collisions, allowing free movement through walls."));
            list.add(new CheatEntry("Emergency Heal", "Grants an automatic mask of health when about to die."));
            list.add(new CheatEntry("Fill Soul", "Instantly fills the Soul gauge."));
            list.add(new CheatEntry("God Mode", "Makes the Knight invincible."));
            list.add(new CheatEntry("Kill All", "Instantly defeats every enemy currently in the area."));
        }

        return list;
    }
}
