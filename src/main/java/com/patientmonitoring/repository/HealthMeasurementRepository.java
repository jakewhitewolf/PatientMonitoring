package com.patientmonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.patientmonitoring.entity.HealthMeasurement;

import java.util.List;

public interface HealthMeasurementRepository extends JpaRepository<HealthMeasurement, Long> {

    List<HealthMeasurement> findByPatientIdOrderByMeasuredAtDesc(Long patientId);
}