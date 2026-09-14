package ru.patientmonitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_measurements")
public class HealthMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Пациент, к которому относится измерение
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Верхнее давление
    @Column(name = "systolic_pressure", nullable = false)
    private Integer systolicPressure;

    // Нижнее давление
    @Column(name = "diastolic_pressure", nullable = false)
    private Integer diastolicPressure;

    // Пульс
    @Column(name = "pulse", nullable = false)
    private Integer pulse;

    // Температура тела
    @Column(name = "temperature", nullable = false)
    private Double temperature;

    // Уровень глюкозы
    @Column(name = "glucose_level", nullable = false)
    private Double glucoseLevel;

    // Сатурация кислорода
    @Column(name = "oxygen_saturation", nullable = false)
    private Integer oxygenSaturation;

    // Дата и время измерения
    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    public HealthMeasurement() {
    }

    public HealthMeasurement(Patient patient,
                             Integer systolicPressure,
                             Integer diastolicPressure,
                             Integer pulse,
                             Double temperature,
                             Double glucoseLevel,
                             Integer oxygenSaturation) {
        this.patient = patient;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.pulse = pulse;
        this.temperature = temperature;
        this.glucoseLevel = glucoseLevel;
        this.oxygenSaturation = oxygenSaturation;
        this.measuredAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (measuredAt == null) {
            measuredAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Integer getSystolicPressure() {
        return systolicPressure;
    }

    public Integer getDiastolicPressure() {
        return diastolicPressure;
    }

    public Integer getPulse() {
        return pulse;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getGlucoseLevel() {
        return glucoseLevel;
    }

    public Integer getOxygenSaturation() {
        return oxygenSaturation;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setSystolicPressure(Integer systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public void setDiastolicPressure(Integer diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setGlucoseLevel(Double glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }

    public void setOxygenSaturation(Integer oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }

    public void setMeasuredAt(LocalDateTime measuredAt) {
        this.measuredAt = measuredAt;
    }
}