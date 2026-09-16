package com.patientmonitoring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class HealthMeasurementCreateRequest {

    @NotNull(message = "Верхнее давление обязательно")
    @Min(value = 50, message = "Верхнее давление слишком низкое")
    @Max(value = 250, message = "Верхнее давление слишком высокое")
    private Integer systolicPressure;

    @NotNull(message = "Нижнее давление обязательно")
    @Min(value = 30, message = "Нижнее давление слишком низкое")
    @Max(value = 150, message = "Нижнее давление слишком высокое")
    private Integer diastolicPressure;

    @NotNull(message = "Пульс обязателен")
    @Min(value = 30, message = "Пульс слишком низкий")
    @Max(value = 220, message = "Пульс слишком высокий")
    private Integer pulse;

    @NotNull(message = "Температура обязательна")
    @DecimalMin(value = "30.0", message = "Температура слишком низкая")
    @DecimalMax(value = "45.0", message = "Температура слишком высокая")
    private Double temperature;

    @NotNull(message = "Уровень глюкозы обязателен")
    @DecimalMin(value = "1.0", message = "Уровень глюкозы слишком низкий")
    @DecimalMax(value = "30.0", message = "Уровень глюкозы слишком высокий")
    private Double glucoseLevel;

    @NotNull(message = "Сатурация обязательна")
    @Min(value = 50, message = "Сатурация слишком низкая")
    @Max(value = 100, message = "Сатурация не может быть больше 100")
    private Integer oxygenSaturation;

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
}