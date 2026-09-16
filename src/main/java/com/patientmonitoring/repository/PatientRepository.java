package com.patientmonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.patientmonitoring.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}