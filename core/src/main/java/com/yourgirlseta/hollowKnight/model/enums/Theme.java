package com.yourgirlseta.hollowKnight.model.enums;

public enum Theme {
    DEFAULT("Menu/Voidheart_menu_BG.png"),
    ALTERNATE("Menu/controller_prompt_bg 2026.png");

    public final String backgroundPath;

    Theme(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }
}
