package ru.patientmonitoring.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.patientmonitoring.dto.HealthMeasurementCreateRequest;
import ru.patientmonitoring.dto.HealthMeasurementResponse;
import ru.patientmonitoring.service.HealthMeasurementService;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/measurements")
public class HealthMeasurementController {

    private final HealthMeasurementService healthMeasurementService;

    public HealthMeasurementController(HealthMeasurementService healthMeasurementService) {
        this.healthMeasurementService = healthMeasurementService;
    }

    @PostMapping
    public HealthMeasurementResponse createMeasurement(@PathVariable Long patientId,
                                                       @Valid @RequestBody HealthMeasurementCreateRequest request) {
        return healthMeasurementService.createMeasurement(patientId, request);
    }

    @GetMapping
    public List<HealthMeasurementResponse> getMeasurementsByPatientId(@PathVariable Long patientId) {
        return healthMeasurementService.getMeasurementsByPatientId(patientId);
    }
}