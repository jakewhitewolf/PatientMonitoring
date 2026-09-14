package ru.patientmonitoring.dto;

import java.time.LocalDateTime;

public class HealthMeasurementResponse {

    private Long id;
    private Long patientId;
    private Integer systolicPressure;
    private Integer diastolicPressure;
    private Integer pulse;
    private Double temperature;
    private Double glucoseLevel;
    private Integer oxygenSaturation;
    private LocalDateTime measuredAt;

    public HealthMeasurementResponse(Long id,
                                     Long patientId,
                                     Integer systolicPressure,
                                     Integer diastolicPressure,
                                     Integer pulse,
                                     Double temperature,
                                     Double glucoseLevel,
                                     Integer oxygenSaturation,
                                     LocalDateTime measuredAt) {
        this.id = id;
        this.patientId = patientId;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.pulse = pulse;
        this.temperature = temperature;
        this.glucoseLevel = glucoseLevel;
        this.oxygenSaturation = oxygenSaturation;
        this.measuredAt = measuredAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
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
}