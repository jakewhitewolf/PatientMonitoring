package ru.patientmonitoring.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class PatientCreateRequest {

    @NotBlank(message = "ФИО пациента не может быть пустым")
    private String fullName;

    private LocalDate birthDate;

    private String diagnosis;

    private String phone;

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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
