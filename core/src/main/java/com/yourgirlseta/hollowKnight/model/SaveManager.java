package com.yourgirlseta.hollowKnight.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaveManager {
    private final DatabaseManager databaseManager;

    public SaveManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void saveGame(int slotId, GameData data) {
        String sql = """

    INSERT INTO save_slots (

        slot_id,
        health,
        spirit,
        map_name,
        spawn_id,
        play_time,
        player_deaths,
        enemies_killed,
        saved_at
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, datetime('now'))

    ON CONFLICT(slot_id) DO UPDATE SET

        health = excluded.health,
        spirit = excluded.spirit,
        map_name = excluded.map_name,
        spawn_id = excluded.spawn_id,
        play_time = excluded.play_time,
        player_deaths = excluded.player_deaths,
        enemies_killed = excluded.enemies_killed,
        saved_at = datetime('now');
    """;

        try (Connection connection = databaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, slotId);
            statement.setInt(2, data.health);
            statement.setInt(3, data.spirit);
            statement.setString(4, data.mapName);
            statement.setString(5, data.spawnId);
            statement.setFloat(6, data.playTime);
            statement.setInt(7, data.playerDeaths);
            statement.setInt(8, data.enemiesKilled);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GameData loadGame(int slotId) {

        String sql =
            "SELECT * FROM save_slots WHERE slot_id = ?";

        try (
            Connection connection =
                databaseManager.connect();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setInt(
                1,
                slotId
            );

            try (
                ResultSet resultSet =
                    statement.executeQuery()
            ) {

                if (!resultSet.next()) {

                    return null;
                }

                GameData data =
                    new GameData();

                data.slotId =
                    resultSet.getInt(
                        "slot_id"
                    );

                data.health =
                    resultSet.getInt(
                        "health"
                    );

                data.spirit =
                    resultSet.getInt(
                        "spirit"
                    );

                data.mapName =
                    resultSet.getString(
                        "map_name"
                    );

                data.spawnId =
                    resultSet.getString(
                        "spawn_id"
                    );

                data.playTime =
                    resultSet.getFloat("play_time");

                data.savedAt =
                    resultSet.getString(
                        "saved_at"
                    );

                data.spirit =
                    resultSet.getInt("spirit");

                data.playerDeaths =
                    resultSet.getInt("player_deaths");

                data.enemiesKilled =
                    resultSet.getInt("enemies_killed");

                return data;
            }

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }

    public boolean hasSave(int slotId) {
        String sql = "SELECT 1 FROM save_slots WHERE slot_id = ?";

        try (Connection connection = databaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, slotId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteSave(int slotId) {
        String sql = "DELETE FROM save_slots WHERE slot_id = ?";

        try (Connection connection = databaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, slotId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

