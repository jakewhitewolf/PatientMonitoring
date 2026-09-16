package com.patientmonitoring.client.model;

public record MeasurementItem(
        Long id,
        Long patientId,
        Integer systolicPressure,
        Integer diastolicPressure,
        Integer pulse,
        Double temperature,
        Double glucoseLevel,
        Integer oxygenSaturation,
        String measuredAt
) {
}