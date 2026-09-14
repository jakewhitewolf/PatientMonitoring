package ru.patientmonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.patientmonitoring.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}