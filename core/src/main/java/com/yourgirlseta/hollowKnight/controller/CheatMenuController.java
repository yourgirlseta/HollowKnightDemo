package com.yourgirlseta.hollowKnight.controller;

import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.CheatManager;
import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.enemy.Enemy;

public class CheatMenuController {

    private final Player player;

    private final CheatManager
        cheatManager;

    public CheatMenuController(
        Player player,
        CheatManager cheatManager
    ) {

        this.player = player;

        this.cheatManager =
            cheatManager;
    }

    public void fillSoul() {

        cheatManager.fillSoul();
    }

    public void killAllEnemies(
        Array<Enemy> enemies
    ) {

        for (Enemy enemy : enemies) {

            enemy.takeDamage(9999);
        }
    }


    public void toggleEmergencyHeal() {

        cheatManager.setEmergencyHeal(
            !cheatManager.isEmergencyHeal()
        );
    }

    public void toggleNoclip() {

        cheatManager.setNoclip(
            !cheatManager.isNoclip()
        );
    }

    public void toggleGodMode() {

        cheatManager.setGodMode(
            !cheatManager.isGodMode()
        );
    }

    public  void toggleBoss() {
        cheatManager.teleportToBoss();
    }
}
