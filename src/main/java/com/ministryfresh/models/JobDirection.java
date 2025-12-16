package com.ministryfresh.models;

import java.time.LocalDateTime;

public class JobDirection {
    private int id;
    private int citizenId;
    private int vacancyId;
    private int centerId; // ID центра занятости
    private String status; // OFFERED, ACCEPTED, REFUSED, COMPLETED
    private LocalDateTime offeredDate;
    private LocalDateTime responseDate;
    private String refusalReason;

    public static final String STATUS_OFFERED = "OFFERED";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_REFUSED = "REFUSED";
    public static final String STATUS_COMPLETED = "COMPLETED";

    public JobDirection() {}

    public JobDirection(int citizenId, int vacancyId, int centerId) {
        this.citizenId = citizenId;
        this.vacancyId = vacancyId;
        this.centerId = centerId;
        this.status = STATUS_OFFERED;
        this.offeredDate = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public int getVacancyId() { return vacancyId; }
    public void setVacancyId(int vacancyId) { this.vacancyId = vacancyId; }

    public int getCenterId() { return centerId; }
    public void setCenterId(int centerId) { this.centerId = centerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOfferedDate() { return offeredDate; }
    public void setOfferedDate(LocalDateTime offeredDate) { this.offeredDate = offeredDate; }

    public LocalDateTime getResponseDate() { return responseDate; }
    public void setResponseDate(LocalDateTime responseDate) { this.responseDate = responseDate; }

    public String getRefusalReason() { return refusalReason; }
    public void setRefusalReason(String refusalReason) { this.refusalReason = refusalReason; }

    public void accept() {
        this.status = STATUS_ACCEPTED;
        this.responseDate = LocalDateTime.now();
    }

    public void refuse(String reason) {
        this.status = STATUS_REFUSED;
        this.responseDate = LocalDateTime.now();
        this.refusalReason = reason;
    }

    public void complete() {
        this.status = STATUS_COMPLETED;
    }

    public String getStatusDisplay() {
        switch (status) {
            case STATUS_OFFERED: return "Предложено";
            case STATUS_ACCEPTED: return "Принято";
            case STATUS_REFUSED: return "Отказано";
            case STATUS_COMPLETED: return "Завершено";
            default: return status;
        }
    }
}
