package com.patientmonitoring.service;

import com.patientmonitoring.config.MedicalThresholdProperties;
import com.patientmonitoring.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.patientmonitoring.dto.HealthMeasurementCreateRequest;
import com.patientmonitoring.dto.HealthMeasurementResponse;
import com.patientmonitoring.repository.AlertRepository;
import com.patientmonitoring.repository.HealthMeasurementRepository;
import com.patientmonitoring.repository.PatientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthMeasurementService {

    private final HealthMeasurementRepository healthMeasurementRepository;
    private final PatientRepository patientRepository;
    private final AlertRepository alertRepository;
    private final MedicalThresholdProperties thresholdProperties;

    public HealthMeasurementService(HealthMeasurementRepository healthMeasurementRepository,
                                    PatientRepository patientRepository,
                                    AlertRepository alertRepository,
                                    MedicalThresholdProperties thresholdProperties) {
        this.healthMeasurementRepository = healthMeasurementRepository;
        this.patientRepository = patientRepository;
        this.alertRepository = alertRepository;
        this.thresholdProperties = thresholdProperties;
    }

    @Transactional
    public HealthMeasurementResponse createMeasurement(Long patientId, HealthMeasurementCreateRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Пациент не найден"));

        HealthMeasurement measurement = new HealthMeasurement(
                patient,
                request.getSystolicPressure(),
                request.getDiastolicPressure(),
                request.getPulse(),
                request.getTemperature(),
                request.getGlucoseLevel(),
                request.getOxygenSaturation()
        );

        HealthMeasurement savedMeasurement = healthMeasurementRepository.save(measurement);

        createAlertIfNeeded(patient, savedMeasurement);

        return toResponse(savedMeasurement);
    }

    public List<HealthMeasurementResponse> getMeasurementsByPatientId(Long patientId) {
        return healthMeasurementRepository.findByPatientIdOrderByMeasuredAtDesc(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void createAlertIfNeeded(Patient patient, HealthMeasurement measurement) {
        List<String> problems = new ArrayList<>();

        if (thresholdProperties.getSystolic().isOutside(measurement.getSystolicPressure())) {
            problems.add("систолическое давление");
        }

        if (thresholdProperties.getDiastolic().isOutside(measurement.getDiastolicPressure())) {
            problems.add("диастолическое давление");
        }

        if (thresholdProperties.getPulse().isOutside(measurement.getPulse())) {
            problems.add("пульс");
        }

        if (thresholdProperties.getTemperature().isOutside(measurement.getTemperature())) {
            problems.add("температура");
        }

        if (thresholdProperties.getGlucose().isOutside(measurement.getGlucoseLevel())) {
            problems.add("уровень глюкозы");
        }

        if (thresholdProperties.getOxygen().isOutside(measurement.getOxygenSaturation())) {
            problems.add("сатурация");
        }

        if (problems.isEmpty()) {
            return;
        }

        int problemCount = problems.size();

        AlertSeverity severity = defineSeverity(problemCount);
        AlertAction action = defineAction(problemCount);

        Alert alert = new Alert();
        alert.setPatient(patient);
        alert.setMeasurement(measurement);
        alert.setSeverity(severity);
        alert.setStatus(AlertStatus.ACTIVE);
        alert.setAction(action);
        alert.setMessage(createAlertMessage(patient, problems, severity, action));

        alertRepository.save(alert);
    }

    private AlertSeverity defineSeverity(int problemCount) {
        if (problemCount >= 4) {
            return AlertSeverity.EMERGENCY;
        }

        if (problemCount >= 2) {
            return AlertSeverity.CRITICAL;
        }

        return AlertSeverity.WARNING;
    }

    private AlertAction defineAction(int problemCount) {
        if (problemCount >= 4) {
            return AlertAction.CALL_AMBULANCE;
        }

        if (problemCount >= 2) {
            return AlertAction.CALL_DOCTOR;
        }

        return AlertAction.NONE;
    }

    private String createAlertMessage(
            Patient patient,
            List<String> problems,
            AlertSeverity severity,
            AlertAction action
    ) {
        String message = "У пациента " + patient.getFullName()
                + " обнаружены отклонения: "
                + String.join(", ", problems) + ".";

        if (severity == AlertSeverity.CRITICAL) {
            message += " Рекомендуется связаться с врачом.";
        }

        if (severity == AlertSeverity.EMERGENCY) {
            message += " Требуется срочная медицинская помощь.";
        }

        return message;
    }

    private HealthMeasurementResponse toResponse(HealthMeasurement measurement) {
        return new HealthMeasurementResponse(
                measurement.getId(),
                measurement.getPatient().getId(),
                measurement.getSystolicPressure(),
                measurement.getDiastolicPressure(),
                measurement.getPulse(),
                measurement.getTemperature(),
                measurement.getGlucoseLevel(),
                measurement.getOxygenSaturation(),
                measurement.getMeasuredAt()
        );
    }
}