package com.patientmonitoring.controller;

import org.springframework.web.bind.annotation.*;
import com.patientmonitoring.dto.AlertResponse;
import com.patientmonitoring.service.AlertService;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<AlertResponse> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/active")
    public List<AlertResponse> getActiveAlerts() {
        return alertService.getActiveAlerts();
    }

    @PatchMapping("/{id}/resolve")
    public AlertResponse resolveAlert(@PathVariable Long id) {
        return alertService.resolveAlert(id);
    }
}