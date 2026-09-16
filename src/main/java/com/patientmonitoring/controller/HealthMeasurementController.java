package com.patientmonitoring.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.patientmonitoring.dto.HealthMeasurementCreateRequest;
import com.patientmonitoring.dto.HealthMeasurementResponse;
import com.patientmonitoring.service.HealthMeasurementService;

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