package com.ministryfresh.repositories;

import com.ministryfresh.database.PostgreSQLConnector;
import com.ministryfresh.models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final PostgreSQLConnector db;

    public UserRepository(PostgreSQLConnector db) {
        this.db = db;
    }

    public void createUsersTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "role VARCHAR(20) NOT NULL CHECK (role IN ('COMPANY', 'EMPLOYMENT_CENTER', 'CITIZEN')), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "last_login TIMESTAMP" +
                ")";

        db.connect();
        try (Statement stmt = db.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица 'users' создана или уже существует");
        }
    }

    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, full_name, role) VALUES (?, ?, ?, ?, ?)";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getRole());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Остальные методы остаются без изменений...
    // (loginUser, isUsernameExists, isEmailExists, updateLastLogin, getAllUsers, mapResultSetToUser)

    // Авторизация пользователя
    public User loginUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);

                    // Обновляем время последнего входа
                    updateLastLogin(user.getId());
                    user.setLastLogin(LocalDateTime.now());

                    return user;
                }
            }
        }
        return null;
    }

    // Обновление времени последнего входа
    private void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    // Проверка существования username
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Проверка существования email
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Получение всех пользователей
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";

        db.connect();
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    // Вспомогательный метод для маппинга ResultSet в User (обновлен для роли)
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }

        return user;
    }
    // Добавление столбца role к существующей таблице
    public void alterUsersTableAddRole() throws SQLException {
        // Сначала проверяем, существует ли столбец role
        String checkSql = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_name = 'users' AND column_name = 'role'";

        db.connect();
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            // Если столбец не существует, добавляем его
            if (!rs.next()) {
                String alterSql = "ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'CITIZEN'";
                stmt.execute(alterSql);

                // Добавляем CHECK ограничение
                String constraintSql = "ALTER TABLE users ADD CONSTRAINT users_role_check " +
                        "CHECK (role IN ('COMPANY', 'EMPLOYMENT_CENTER', 'CITIZEN'))";
                stmt.execute(constraintSql);

                System.out.println("Столбец 'role' успешно добавлен в таблицу 'users'");
            } else {
                System.out.println("Столбец 'role' уже существует в таблице 'users'");
            }
        }
    }
}