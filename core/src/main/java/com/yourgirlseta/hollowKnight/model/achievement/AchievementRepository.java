package com.yourgirlseta.hollowKnight.model.achievement;

import com.yourgirlseta.hollowKnight.model.DatabaseManager;
import com.yourgirlseta.hollowKnight.model.enums.AchievementType;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AchievementRepository {

    private final DatabaseManager databaseManager;

    public AchievementRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Set<AchievementType> loadUnlockedAchievements() {

        Set<AchievementType> result = new HashSet<>();
        String sql = "SELECT achievement_id FROM achievements";

        try (Connection connection = databaseManager.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String id = resultSet.getString("achievement_id");

                try {
                    result.add(AchievementType.valueOf(id));
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown achievement id in DB: " + id);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void saveUnlockedAchievement(AchievementType type) {

        String sql = """
            INSERT INTO achievements (achievement_id, unlocked_at)
            VALUES (?, datetime('now'))
            ON CONFLICT(achievement_id) DO NOTHING;
        """;

        try (Connection connection = databaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type.name());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
