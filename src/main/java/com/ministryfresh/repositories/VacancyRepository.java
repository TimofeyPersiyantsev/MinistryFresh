package com.ministryfresh.repositories;

import com.ministryfresh.database.PostgreSQLConnector;
import com.ministryfresh.models.Vacancy;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VacancyRepository {
    private final PostgreSQLConnector db;

    public VacancyRepository(PostgreSQLConnector db) {
        this.db = db;
    }

    public void createVacanciesTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS vacancies (" +
                "id SERIAL PRIMARY KEY, " +
                "company_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE, " +
                "title VARCHAR(200) NOT NULL, " +
                "description TEXT NOT NULL, " +
                "requirements TEXT, " +
                "responsibilities TEXT, " +
                "salary DECIMAL(10, 2), " +
                "location VARCHAR(100), " +
                "employment_type VARCHAR(20) NOT NULL, " +
                "experience_level VARCHAR(20) NOT NULL, " +
                "is_active BOOLEAN DEFAULT TRUE, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.connect();
        try (Statement stmt = db.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица 'vacancies' создана или уже существует");
        }
    }

    public int createVacancy(Vacancy vacancy) throws SQLException {
        String sql = "INSERT INTO vacancies (company_id, title, description, requirements, " +
                "responsibilities, salary, location, employment_type, experience_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, vacancy.getCompanyId());
            pstmt.setString(2, vacancy.getTitle());
            pstmt.setString(3, vacancy.getDescription());
            pstmt.setString(4, vacancy.getRequirements());
            pstmt.setString(5, vacancy.getResponsibilities());

            if (vacancy.getSalary() != null) {
                pstmt.setBigDecimal(6, vacancy.getSalary());
            } else {
                pstmt.setNull(6, Types.DECIMAL);
            }

            pstmt.setString(7, vacancy.getLocation());
            pstmt.setString(8, vacancy.getEmploymentType());
            pstmt.setString(9, vacancy.getExperienceLevel());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Не удалось создать вакансию");
        }
    }

    public Vacancy getVacancyById(int vacancyId) throws SQLException {
        String sql = "SELECT v.*, u.full_name as company_name FROM vacancies v " +
                "JOIN users u ON v.company_id = u.id WHERE v.id = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, vacancyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVacancy(rs);
                }
            }
        }
        return null;
    }

    public List<Vacancy> getAllActiveVacancies() throws SQLException {
        List<Vacancy> vacancies = new ArrayList<>();
        String sql = "SELECT v.*, u.full_name as company_name FROM vacancies v " +
                "JOIN users u ON v.company_id = u.id " +
                "WHERE v.is_active = TRUE ORDER BY v.created_at DESC";

        db.connect();
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vacancies.add(mapResultSetToVacancy(rs));
            }
        }
        return vacancies;
    }

    public List<Vacancy> getCompanyVacancies(int companyId) throws SQLException {
        List<Vacancy> vacancies = new ArrayList<>();
        String sql = "SELECT v.*, u.full_name as company_name FROM vacancies v " +
                "JOIN users u ON v.company_id = u.id " +
                "WHERE v.company_id = ? ORDER BY v.created_at DESC";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, companyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vacancies.add(mapResultSetToVacancy(rs));
                }
            }
        }
        return vacancies;
    }

    public boolean updateVacancy(Vacancy vacancy) throws SQLException {
        String sql = "UPDATE vacancies SET title = ?, description = ?, requirements = ?, " +
                "responsibilities = ?, salary = ?, location = ?, employment_type = ?, " +
                "experience_level = ?, is_active = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ? AND company_id = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, vacancy.getTitle());
            pstmt.setString(2, vacancy.getDescription());
            pstmt.setString(3, vacancy.getRequirements());
            pstmt.setString(4, vacancy.getResponsibilities());

            if (vacancy.getSalary() != null) {
                pstmt.setBigDecimal(5, vacancy.getSalary());
            } else {
                pstmt.setNull(5, Types.DECIMAL);
            }

            pstmt.setString(6, vacancy.getLocation());
            pstmt.setString(7, vacancy.getEmploymentType());
            pstmt.setString(8, vacancy.getExperienceLevel());
            pstmt.setBoolean(9, vacancy.isActive());
            pstmt.setInt(10, vacancy.getId());
            pstmt.setInt(11, vacancy.getCompanyId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteVacancy(int vacancyId, int companyId) throws SQLException {
        String sql = "DELETE FROM vacancies WHERE id = ? AND company_id = ?";

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, vacancyId);
            pstmt.setInt(2, companyId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Vacancy> searchVacancies(String keyword, String location,
                                         BigDecimal minSalary, String employmentType) throws SQLException {
        List<Vacancy> vacancies = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT v.*, u.full_name as company_name FROM vacancies v " +
                        "JOIN users u ON v.company_id = u.id " +
                        "WHERE v.is_active = TRUE "
        );

        List<Object> params = new ArrayList<>();
        int paramIndex = 1;

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(v.title) LIKE ? OR LOWER(v.description) LIKE ?) ");
            String searchTerm = "%" + keyword.toLowerCase() + "%";
            params.add(searchTerm);
            params.add(searchTerm);
        }

        if (location != null && !location.trim().isEmpty()) {
            sql.append("AND LOWER(v.location) LIKE ? ");
            params.add("%" + location.toLowerCase() + "%");
        }

        if (minSalary != null) {
            sql.append("AND v.salary >= ? ");
            params.add(minSalary);
        }

        if (employmentType != null && !employmentType.trim().isEmpty()) {
            sql.append("AND v.employment_type = ? ");
            params.add(employmentType);
        }

        sql.append("ORDER BY v.created_at DESC");

        db.connect();
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vacancies.add(mapResultSetToVacancy(rs));
                }
            }
        }
        return vacancies;
    }

    private Vacancy mapResultSetToVacancy(ResultSet rs) throws SQLException {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(rs.getInt("id"));
        vacancy.setCompanyId(rs.getInt("company_id"));
        vacancy.setTitle(rs.getString("title"));
        vacancy.setDescription(rs.getString("description"));
        vacancy.setRequirements(rs.getString("requirements"));
        vacancy.setResponsibilities(rs.getString("responsibilities"));

        BigDecimal salary = rs.getBigDecimal("salary");
        if (!rs.wasNull()) {
            vacancy.setSalary(salary);
        }

        vacancy.setLocation(rs.getString("location"));
        vacancy.setEmploymentType(rs.getString("employment_type"));
        vacancy.setExperienceLevel(rs.getString("experience_level"));
        vacancy.setActive(rs.getBoolean("is_active"));
        vacancy.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            vacancy.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return vacancy;
    }
}
