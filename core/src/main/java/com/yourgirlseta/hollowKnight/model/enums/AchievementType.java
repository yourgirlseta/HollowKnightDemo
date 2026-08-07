package com.yourgirlseta.hollowKnight.model.enums;

public enum AchievementType {
    COMPLETION(
        "Completion", "Achèvement",
        "Achievements/achievement__0006_ending_A.png",
        "Defeat the False Knight and complete the game.",
        "Vainquez le Faux Chevalier et terminez le jeu."
    ),
    SPEEDRUN(
        "Speedrun", "Course contre la montre",
        "Achievements/achievement_ultra_fast_finish.png",
        "Complete the game in under 10 minutes.",
        "Terminez le jeu en moins de 10 minutes."
    ),
    TRUE_HUNTER(
        "True Hunter", "Vrai Chasseur",
        "Achievements/achievement_Hunter_Marks.png",
        "Defeat at least one of every enemy type in the game.",
        "Vainquez au moins un ennemi de chaque type dans le jeu."
    ),
    DEFEAT_FALSE_KNIGHT(
        "Defeat False Knight", "Vaincre le Faux Chevalier",
        "Achievements/achievement__0031_false_knight_dream.png",
        "Defeat the False Knight boss.",
        "Vainquez le boss Faux Chevalier."
    ),
    UNTOUCHABLE(
        "Untouchable", "Intouchable",
        "Achievements/achievement__0031_false_knight_dream.png",
        "Defeat the False Knight without taking any damage.",
        "Vainquez le Faux Chevalier sans subir aucun dégât."
    );

    public final String displayNameEn;
    public final String displayNameFr;
    public final String iconPath;
    public final String descriptionEn;
    public final String descriptionFr;

    AchievementType(
        String displayNameEn,
        String displayNameFr,
        String iconPath,
        String descriptionEn,
        String descriptionFr
    ) {
        this.displayNameEn = displayNameEn;
        this.displayNameFr = displayNameFr;
        this.iconPath = iconPath;
        this.descriptionEn = descriptionEn;
        this.descriptionFr = descriptionFr;
    }
}
