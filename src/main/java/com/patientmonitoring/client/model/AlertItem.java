package com.patientmonitoring.client.model;

public record AlertItem(
        Long id,
        Long patientId,
        String patientFullName,
        Long measurementId,
        String message,
        String severity,
        String status,
        String action,
        String createdAt
) {
}