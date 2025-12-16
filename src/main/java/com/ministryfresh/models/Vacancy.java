package com.ministryfresh.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Vacancy {
    private int id;
    private int companyId; // ID компании, создавшей вакансию
    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private BigDecimal salary;
    private String location;
    private String employmentType; // FULL_TIME, PART_TIME, REMOTE, etc.
    private String experienceLevel; // JUNIOR, MIDDLE, SENIOR
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Vacancy() {}

    public Vacancy(int companyId, String title, String description, String requirements,
                   String responsibilities, BigDecimal salary, String location,
                   String employmentType, String experienceLevel) {
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.responsibilities = responsibilities;
        this.salary = salary;
        this.location = location;
        this.employmentType = employmentType;
        this.experienceLevel = experienceLevel;
        this.isActive = true;
    }

    public static final String EMPLOYMENT_FULL_TIME = "FULL_TIME";
    public static final String EMPLOYMENT_PART_TIME = "PART_TIME";
    public static final String EMPLOYMENT_REMOTE = "REMOTE";
    public static final String EMPLOYMENT_CONTRACT = "CONTRACT";

    public static final String EXPERIENCE_JUNIOR = "JUNIOR";
    public static final String EXPERIENCE_MIDDLE = "MIDDLE";
    public static final String EXPERIENCE_SENIOR = "SENIOR";
    public static final String EXPERIENCE_NO_EXPERIENCE = "NO_EXPERIENCE";

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getEmploymentTypeDisplay() {
        switch (employmentType) {
            case EMPLOYMENT_FULL_TIME: return "Полная занятость";
            case EMPLOYMENT_PART_TIME: return "Частичная занятость";
            case EMPLOYMENT_REMOTE: return "Удаленная работа";
            case EMPLOYMENT_CONTRACT: return "Контракт";
            default: return employmentType;
        }
    }

    public String getExperienceLevelDisplay() {
        switch (experienceLevel) {
            case EXPERIENCE_JUNIOR: return "Начальный уровень (Junior)";
            case EXPERIENCE_MIDDLE: return "Средний уровень (Middle)";
            case EXPERIENCE_SENIOR: return "Высокий уровень (Senior)";
            case EXPERIENCE_NO_EXPERIENCE: return "Без опыта";
            default: return experienceLevel;
        }
    }

    @Override
    public String toString() {
        return String.format("Vacancy{id=%d, title='%s', companyId=%d, salary=%s, location='%s'}",
                id, title, companyId, salary, location);
    }
}
