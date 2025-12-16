package com.ministryfresh.models;

import java.time.LocalDateTime;

public class CitizenEmployment {
    private int id;
    private int citizenId; // ID гражданина
    private boolean isRegistered; // Зарегистрирован на бирже
    private int attemptsLeft; // Осталось попыток (макс 3)
    private LocalDateTime registrationDate; // Дата постановки на учёт
    private LocalDateTime lastAttemptDate; // Дата последнего направления
    private boolean receivesBenefits; // Получает выплаты
    private String status; // ACTIVE, SUSPENDED, REMOVED

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_SUSPENDED = "SUSPENDED";
    public static final String STATUS_REMOVED = "REMOVED";

    public CitizenEmployment() {
        this.attemptsLeft = 3;
        this.isRegistered = false;
        this.receivesBenefits = false;
        this.status = STATUS_ACTIVE;
    }

    public CitizenEmployment(int citizenId) {
        this();
        this.citizenId = citizenId;
        this.registrationDate = LocalDateTime.now();
    }

    public void registerForEmployment() {
        this.isRegistered = true;
        this.registrationDate = LocalDateTime.now();
        this.receivesBenefits = true;
        this.attemptsLeft = 3;
        this.status = STATUS_ACTIVE;
    }

    public boolean sendToInterview() {
        if (!isRegistered || attemptsLeft <= 0) {
            return false;
        }

        this.lastAttemptDate = LocalDateTime.now();
        return true;
    }

    public boolean refuseJob() {
        if (!isRegistered || attemptsLeft <= 0) {
            return false;
        }

        this.attemptsLeft--;
        this.lastAttemptDate = LocalDateTime.now();

        if (attemptsLeft <= 0) {
            removeFromEmployment();
        }

        return true;
    }

    public boolean getHired() {
        if (!isRegistered) {
            return false;
        }

        this.isRegistered = false;
        this.receivesBenefits = false;
        this.status = "HIRED";
        return true;
    }

    private void removeFromEmployment() {
        this.isRegistered = false;
        this.receivesBenefits = false;
        this.status = STATUS_REMOVED;
    }

    public void reinstate() {
        this.isRegistered = true;
        this.attemptsLeft = 3;
        this.status = STATUS_ACTIVE;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public boolean isRegistered() { return isRegistered; }
    public void setRegistered(boolean registered) { isRegistered = registered; }

    public int getAttemptsLeft() { return attemptsLeft; }
    public void setAttemptsLeft(int attemptsLeft) { this.attemptsLeft = attemptsLeft; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getLastAttemptDate() { return lastAttemptDate; }
    public void setLastAttemptDate(LocalDateTime lastAttemptDate) { this.lastAttemptDate = lastAttemptDate; }

    public boolean isReceivesBenefits() { return receivesBenefits; }
    public void setReceivesBenefits(boolean receivesBenefits) { this.receivesBenefits = receivesBenefits; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusDisplay() {
        switch (status) {
            case STATUS_ACTIVE: return "Активен";
            case STATUS_SUSPENDED: return "Приостановлен";
            case STATUS_REMOVED: return "Снят с учёта";
            case "HIRED": return "Трудоустроен";
            default: return status;
        }
    }
}
