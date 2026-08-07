package com.yourgirlseta.hollowKnight.controller;

import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.model.GameData;
import com.yourgirlseta.hollowKnight.model.SaveManager;
import com.yourgirlseta.hollowKnight.view.GameScreen;
import com.yourgirlseta.hollowKnight.view.MainMenuScreen;

public class StartGameMenuController {
    private final Main game;
    private final SaveManager saveManager;

    public StartGameMenuController(Main game) {
        this.game = game;
        this.saveManager = game.getSaveManager();
    }

    public String getSlotText(
        int slotId
    ) {

        if (
            !saveManager.hasSave(slotId)
        ) {

            return
                "Slot " +
                    slotId +
                    " - Empty";
        }

        GameData data =
            saveManager.loadGame(
                slotId
            );

        if (data == null) {

            return
                "Corrupted Save";
        }

        long totalSeconds =
            (long)data.playTime;

        long hours =
            totalSeconds / 3600;

        long minutes =
            (totalSeconds % 3600) / 60;

        String timeText =
            String.format(
                "%02dh %02dm",
                hours,
                minutes
            );

        return
                "Map: " +
                data.mapName +

                " | HP: " +
                data.health +

                " | Spirit: " +
                data.spirit +

                " | Time: " +
                timeText;
    }

    public void onNewGameClicked() {
        int targetSlot = findFirstEmptySlot();

        if (targetSlot == -1) {
            targetSlot = 4;
            saveManager.deleteSave(targetSlot);
        }

        GameData data = GameData.createNewGame();
        data.slotId = targetSlot;

        saveManager.saveGame(targetSlot, data);
        game.setScreen(new GameScreen(game, data));
    }

    public void onSlotClicked(int slotId) {

        GameData loadedData =
            saveManager.loadGame(slotId);

        if (loadedData == null) {
            return;
        }

        game.setScreen(
            new GameScreen(
                game,
                loadedData
            )
        );
    }

    public void onBackClicked() {
        game.setScreen(new MainMenuScreen(game));
    }

    private int findFirstEmptySlot() {
        for (int slotId = 1; slotId <= 4; slotId++) {
            if (!saveManager.hasSave(slotId)) {
                return slotId;
            }
        }
        return -1;
    }
}


