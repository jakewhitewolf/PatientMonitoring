package com.patientmonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.patientmonitoring.entity.Alert;
import com.patientmonitoring.entity.AlertStatus;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByStatusOrderByCreatedAtDesc(AlertStatus status);

    List<Alert> findAllByOrderByCreatedAtDesc();
}