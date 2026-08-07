package com.yourgirlseta.hollowKnight.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:game_saves.db";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void initializeDatabase() {

        String saveSlotsSql = """
        CREATE TABLE IF NOT EXISTS save_slots (

            slot_id INTEGER PRIMARY KEY,

            health INTEGER NOT NULL,
            spirit INTEGER NOT NULL,

            map_name TEXT NOT NULL,
            spawn_id TEXT NOT NULL,

            play_time REAL NOT NULL,

            player_deaths INTEGER NOT NULL,

            enemies_killed INTEGER NOT NULL,

            saved_at TEXT NOT NULL
        );
        """;

        String achievementsSql = """
        CREATE TABLE IF NOT EXISTS achievements (

            achievement_id TEXT PRIMARY KEY,
            unlocked_at TEXT NOT NULL
        );
        """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.execute(saveSlotsSql);
            statement.execute(achievementsSql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
