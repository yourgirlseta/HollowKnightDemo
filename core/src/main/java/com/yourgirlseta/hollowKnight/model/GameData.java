package com.yourgirlseta.hollowKnight.model;

public class GameData {
    public int slotId;
    public int health;
    public int spirit;
    public String mapName;
    public String spawnId;
    public float playTime;
    public String savedAt;
    public int playerDeaths;
    public int enemiesKilled;

    public static GameData createNewGame() {
        GameData data = new GameData();
        data.health = 5;
        data.spirit = 0;
        data.mapName = "Map1";
        data.spawnId = "start";
        data.playTime = 0;
        data.savedAt = "";
        data.playerDeaths = 0;
        data.enemiesKilled = 0;
        return data;
    }
}

