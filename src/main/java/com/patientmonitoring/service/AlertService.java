package com.patientmonitoring.service;

import org.springframework.stereotype.Service;
import com.patientmonitoring.dto.AlertResponse;
import com.patientmonitoring.entity.Alert;
import com.patientmonitoring.entity.AlertStatus;
import com.patientmonitoring.repository.AlertRepository;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<AlertResponse> getAllAlerts() {
        return alertRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AlertResponse> getActiveAlerts() {
        return alertRepository.findByStatusOrderByCreatedAtDesc(AlertStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AlertResponse resolveAlert(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Предупреждение не найдено"));

        alert.setStatus(AlertStatus.RESOLVED);

        Alert savedAlert = alertRepository.save(alert);

        return toResponse(savedAlert);
    }

    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getPatient().getId(),
                alert.getPatient().getFullName(),
                alert.getMeasurement().getId(),
                alert.getMessage(),
                alert.getSeverity(),
                alert.getStatus(),
                alert.getCreatedAt()
        );
    }
}