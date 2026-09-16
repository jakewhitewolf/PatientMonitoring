package com.patientmonitoring.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientResponse {

    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private String diagnosis;
    private String phone;
    private LocalDateTime createdAt;

    public PatientResponse(Long id, String fullName, LocalDate birthDate, String diagnosis, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.diagnosis = diagnosis;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
