package com.patientmonitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Пациент, по которому создано предупреждение
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Измерение, из-за которого появилось предупреждение
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "measurement_id", nullable = false)
    private HealthMeasurement measurement;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Alert() {
    }

    public Alert(Patient patient, HealthMeasurement measurement, String message, AlertSeverity severity) {
        this.patient = patient;
        this.measurement = measurement;
        this.message = message;
        this.severity = severity;
        this.status = AlertStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = AlertStatus.ACTIVE;
        }
    }

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public HealthMeasurement getMeasurement() {
        return measurement;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setMeasurement(HealthMeasurement measurement) {
        this.measurement = measurement;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}