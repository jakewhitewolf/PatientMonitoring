package com.patientmonitoring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.patientmonitoring.dto.HealthMeasurementCreateRequest;
import com.patientmonitoring.dto.HealthMeasurementResponse;
import com.patientmonitoring.entity.Alert;
import com.patientmonitoring.entity.AlertSeverity;
import com.patientmonitoring.entity.HealthMeasurement;
import com.patientmonitoring.entity.Patient;
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

    public HealthMeasurementService(HealthMeasurementRepository healthMeasurementRepository,
                                    PatientRepository patientRepository,
                                    AlertRepository alertRepository) {
        this.healthMeasurementRepository = healthMeasurementRepository;
        this.patientRepository = patientRepository;
        this.alertRepository = alertRepository;
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
        AlertSeverity severity = AlertSeverity.WARNING;

        if (measurement.getSystolicPressure() > 160 || measurement.getSystolicPressure() < 80) {
            problems.add("критическое отклонение верхнего давления");
            severity = AlertSeverity.CRITICAL;
        } else if (measurement.getSystolicPressure() > 140 || measurement.getSystolicPressure() < 90) {
            problems.add("отклонение верхнего давления");
        }

        if (measurement.getDiastolicPressure() > 100 || measurement.getDiastolicPressure() < 50) {
            problems.add("критическое отклонение нижнего давления");
            severity = AlertSeverity.CRITICAL;
        } else if (measurement.getDiastolicPressure() > 90 || measurement.getDiastolicPressure() < 60) {
            problems.add("отклонение нижнего давления");
        }

        if (measurement.getPulse() > 130 || measurement.getPulse() < 45) {
            problems.add("критическое отклонение пульса");
            severity = AlertSeverity.CRITICAL;
        } else if (measurement.getPulse() > 100 || measurement.getPulse() < 55) {
            problems.add("отклонение пульса");
        }

        if (measurement.getTemperature() > 39.0 || measurement.getTemperature() < 35.0) {
            problems.add("критическое отклонение температуры");
            severity = AlertSeverity.CRITICAL;
        } else if (measurement.getTemperature() > 37.5 || measurement.getTemperature() < 36.0) {
            problems.add("отклонение температуры");
        }

        if (measurement.getGlucoseLevel() > 13.0 || measurement.getGlucoseLevel() < 3.0) {
            problems.add("критическое отклонение уровня глюкозы");
            severity = AlertSeverity.CRITICAL;
        } else if (measurement.getGlucoseLevel() > 10.0 || measurement.getGlucoseLevel() < 3.9) {
            problems.add("отклонение уровня глюкозы");
        }

        if (measurement.getOxygenSaturation() < 90) {
            problems.add("критическое снижение сатурации");
            severity = AlertSeverity.CRITICAL;
        } else if (measurement.getOxygenSaturation() < 95) {
            problems.add("снижение сатурации");
        }

        if (!problems.isEmpty()) {
            String message = "У пациента " + patient.getFullName() + " обнаружены отклонения: "
                    + String.join(", ", problems) + ".";

            Alert alert = new Alert(patient, measurement, message, severity);
            alertRepository.save(alert);
        }
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