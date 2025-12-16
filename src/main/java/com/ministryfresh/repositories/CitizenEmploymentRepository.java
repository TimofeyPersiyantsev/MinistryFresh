package com.ministryfresh.repositories;

import com.ministryfresh.database.PostgreSQLConnector;
import com.ministryfresh.models.CitizenEmployment;
import com.ministryfresh.models.JobDirection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitizenEmploymentRepository {
    private final PostgreSQLConnector db;

    public CitizenEmploymentRepository(PostgreSQLConnector db) {
        this.db = db;
    }

    public void createEmploymentTables() throws SQLException {
        String employmentSql = "CREATE TABLE IF NOT EXISTS citizen_employment (" +
                "id SERIAL PRIMARY KEY, " +
                "citizen_id INTEGER UNIQUE NOT NULL REFERENCES users(id), " +
                "is_registered BOOLEAN DEFAULT FALSE, " +
                "attempts_left INTEGER DEFAULT 3, " +
                "registration_date TIMESTAMP, " +
                "last_attempt_date TIMESTAMP, " +
                "receives_benefits BOOLEAN DEFAULT FALSE, " +
                "status VARCHAR(20) DEFAULT 'ACTIVE', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String directionsSql = "CREATE TABLE IF NOT EXISTS job_directions (" +
                "id SERIAL PRIMARY KEY, " +
                "citizen_id INTEGER NOT NULL REFERENCES users(id), " +
                "vacancy_id INTEGER NOT NULL REFERENCES vacancies(id), " +
                "center_id INTEGER NOT NULL REFERENCES users(id), " +
                "status VARCHAR(20) DEFAULT 'OFFERED', " +
                "offered_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "response_date TIMESTAMP, " +
                "refusal_reason TEXT, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.connect();
        try (Statement stmt = db.getConnection().createStatement()) {
            stmt.execute(employmentSql);
            stmt.execute(directionsSql);
            System.out.println("Таблицы для учёта занятости созданы");
        }
    }

    public boolean registerCitizen(int citizenId) throws SQLException {
        String sql = "INSERT INTO citizen_employment (citizen_id, is_registered, attempts_left, " +
                "registration_date, receives_benefits, status) " +
                "VALUES (?, TRUE, 3, CURRENT_TIMESTAMP, TRUE, 'ACTIVE') " +
                "ON CONFLICT (citizen_id) DO UPDATE SET " +
                "is_registered = TRUE, attempts_left = 3, " +
                "registration_date = CURRENT_TIMESTAMP, " +
                "receives_benefits = TRUE, status = 'ACTIVE'";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, citizenId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public CitizenEmployment getCitizenEmployment(int citizenId) throws SQLException {
        String sql = "SELECT * FROM citizen_employment WHERE citizen_id = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, citizenId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCitizenEmployment(rs);
                }
            }
        }
        return null;
    }

    public int createJobDirection(JobDirection direction) throws SQLException {
        String sql = "INSERT INTO job_directions (citizen_id, vacancy_id, center_id, status, offered_date) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, direction.getCitizenId());
            pstmt.setInt(2, direction.getVacancyId());
            pstmt.setInt(3, direction.getCenterId());
            pstmt.setString(4, direction.getStatus());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Не удалось создать направление");
        }
    }

    public boolean updateJobDirectionStatus(int directionId, String status, String refusalReason) throws SQLException {
        String sql = "UPDATE job_directions SET status = ?, response_date = CURRENT_TIMESTAMP, " +
                "refusal_reason = ? WHERE id = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, refusalReason);
            pstmt.setInt(3, directionId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updateCitizenAttempts(int citizenId, int attemptsLeft, boolean receivesBenefits, String status) throws SQLException {
        String sql = "UPDATE citizen_employment SET attempts_left = ?, receives_benefits = ?, " +
                "status = ?, last_attempt_date = CURRENT_TIMESTAMP WHERE citizen_id = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, attemptsLeft);
            pstmt.setBoolean(2, receivesBenefits);
            pstmt.setString(3, status);
            pstmt.setInt(4, citizenId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<JobDirection> getCitizenDirections(int citizenId) throws SQLException {
        List<JobDirection> directions = new ArrayList<>();
        String sql = "SELECT jd.*, v.title as vacancy_title, u.full_name as center_name " +
                "FROM job_directions jd " +
                "JOIN vacancies v ON jd.vacancy_id = v.id " +
                "JOIN users u ON jd.center_id = u.id " +
                "WHERE jd.citizen_id = ? ORDER BY jd.offered_date DESC";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, citizenId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    directions.add(mapResultSetToJobDirection(rs));
                }
            }
        }
        return directions;
    }

    public List<CitizenEmployment> getRegisteredCitizens() throws SQLException {
        List<CitizenEmployment> citizens = new ArrayList<>();
        String sql = "SELECT ce.*, u.full_name, u.email " +
                "FROM citizen_employment ce " +
                "JOIN users u ON ce.citizen_id = u.id " +
                "WHERE ce.is_registered = TRUE AND ce.attempts_left > 0 " +
                "ORDER BY ce.registration_date";

        db.connect();
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                citizens.add(mapResultSetToCitizenEmployment(rs));
            }
        }
        return citizens;
    }

    private CitizenEmployment mapResultSetToCitizenEmployment(ResultSet rs) throws SQLException {
        CitizenEmployment employment = new CitizenEmployment();
        employment.setId(rs.getInt("id"));
        employment.setCitizenId(rs.getInt("citizen_id"));
        employment.setRegistered(rs.getBoolean("is_registered"));
        employment.setAttemptsLeft(rs.getInt("attempts_left"));
        employment.setReceivesBenefits(rs.getBoolean("receives_benefits"));
        employment.setStatus(rs.getString("status"));

        Timestamp regDate = rs.getTimestamp("registration_date");
        if (regDate != null) {
            employment.setRegistrationDate(regDate.toLocalDateTime());
        }

        Timestamp lastAttempt = rs.getTimestamp("last_attempt_date");
        if (lastAttempt != null) {
            employment.setLastAttemptDate(lastAttempt.toLocalDateTime());
        }

        return employment;
    }

    private JobDirection mapResultSetToJobDirection(ResultSet rs) throws SQLException {
        JobDirection direction = new JobDirection();
        direction.setId(rs.getInt("id"));
        direction.setCitizenId(rs.getInt("citizen_id"));
        direction.setVacancyId(rs.getInt("vacancy_id"));
        direction.setCenterId(rs.getInt("center_id"));
        direction.setStatus(rs.getString("status"));

        Timestamp offeredDate = rs.getTimestamp("offered_date");
        if (offeredDate != null) {
            direction.setOfferedDate(offeredDate.toLocalDateTime());
        }

        Timestamp responseDate = rs.getTimestamp("response_date");
        if (responseDate != null) {
            direction.setResponseDate(responseDate.toLocalDateTime());
        }

        direction.setRefusalReason(rs.getString("refusal_reason"));

        return direction;
    }
}
