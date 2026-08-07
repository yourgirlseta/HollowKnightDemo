package com.yourgirlseta.hollowKnight.model.enums;

public enum CharmType {
    SOUL_CATCHER(
        "Soul Catcher", 1, "Inventory & UI/Charms/Soul Catcher - _0001_charm_more_soul.png",
        "Increases the amount of Soul gained per successful Nail strike.",
        "Augmente la quantité d'Âme gagnée à chaque coup d'épée réussi."
    ),
    DASHMASTER(
        "Dashmaster", 1, "Inventory & UI/Charms/Dashmaster - _0011_charm_generic_03.png",
        "Allows the Knight to dash again after a shorter cooldown.",
        "Permet au Chevalier de faire une nouvelle ruée après un temps de recharge plus court."
    ),
    UNBREAKABLE_STRENGTH(
        "Unbreakable Strength", 1, "Inventory & UI/Charms/Unbreakable Strength_0002_charm_glass_attack_up_full.png",
        "Strengthens the Knight's arms, increasing Nail damage.",
        "Renforce les bras du Chevalier, augmentant les dégâts de l'épée."
    ),
    QUICK_SLASH(
        "Quick Slash", 1, "Inventory & UI/Charms/Quick Slash - _0003_charm_nail_slash_speed_up.png",
        "Greatly increases attack speed and reduces the cooldown between Nail strikes.",
        "Augmente considérablement la vitesse d'attaque et réduit le temps de recharge entre les coups d'épée."
    ),
    QUICK_FOCUS(
        "Quick Focus", 1, "Inventory & UI/Charms/Quick Focus - _0005_charm_fast_focus.png",
        "Increases focusing speed, shortening the time needed to heal.",
        "Augmente la vitesse de concentration, réduisant le temps nécessaire pour se soigner."
    ),
    HEAVY_BLOW(
        "Heavy Blow", 1, "Inventory & UI/Charms/Heavy Blow - _0008_charm_nail_damage_up.png",
        "Increases knockback force, sending enemies flying further after being hit.",
        "Augmente la force de recul, projetant les ennemis plus loin après avoir été touchés."
    ),
    SHARP_SHADOW(
        "Sharp Shadow", 1,"Inventory & UI/Charms/Sharp Shadow - charm_shade_impact.png",
        "While dashing, pass through enemies dealing damage and taking none. Also increases dash length by 20%.",
        "Pendant la ruée, traverse les ennemis en infligeant des dégâts sans en subir. Augmente aussi la longueur de la ruée de 20%."
    ),
    VOID_HEART(
        "Void Heart", 1, "Inventory & UI/Charms/Void Heart - charm_black.png",
        "Empowers spells, increasing their damage by 50% and unlocking a darker animation for each.",
        "Renforce les sorts, augmentant leurs dégâts de 50% et débloquant une animation plus sombre pour chacun."
    );

    public final String displayName;
    public final int notchCost;
    public final String iconPath;
    public final String descriptionEn;
    public final String descriptionFr;

    CharmType(String displayName, int notchCost, String iconPath, String descriptionEn, String descriptionFr) {
        this.displayName = displayName;
        this.notchCost = notchCost;
        this.iconPath = iconPath;
        this.descriptionEn = descriptionEn;
        this.descriptionFr = descriptionFr;
    }
}
