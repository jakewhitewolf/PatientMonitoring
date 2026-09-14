package ru.patientmonitoring.dto;

import ru.patientmonitoring.entity.AlertSeverity;
import ru.patientmonitoring.entity.AlertStatus;

import java.time.LocalDateTime;

public class AlertResponse {

    private Long id;
    private Long patientId;
    private String patientFullName;
    private Long measurementId;
    private String message;
    private AlertSeverity severity;
    private AlertStatus status;
    private LocalDateTime createdAt;

    public AlertResponse(Long id,
                         Long patientId,
                         String patientFullName,
                         Long measurementId,
                         String message,
                         AlertSeverity severity,
                         AlertStatus status,
                         LocalDateTime createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.patientFullName = patientFullName;
        this.measurementId = measurementId;
        this.message = message;
        this.severity = severity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public Long getMeasurementId() {
        return measurementId;
    }

    public String getMessage() {
        return message;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}