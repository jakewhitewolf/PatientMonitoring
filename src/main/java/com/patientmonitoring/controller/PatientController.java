package com.patientmonitoring.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.patientmonitoring.dto.PatientCreateRequest;
import com.patientmonitoring.dto.PatientResponse;
import com.patientmonitoring.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public PatientResponse createPatient(@Valid @RequestBody PatientCreateRequest request) {
        return patientService.createPatient(request);
    }

    @GetMapping
    public List<PatientResponse> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public PatientResponse getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }
}