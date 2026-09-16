package com.patientmonitoring.client.model;

public record PatientItem(
        Long id,
        String fullName,
        String birthDate,
        String diagnosis,
        String phone,
        String createdAt
) {
}