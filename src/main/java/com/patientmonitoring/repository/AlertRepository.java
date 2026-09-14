package ru.patientmonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.patientmonitoring.entity.Alert;
import ru.patientmonitoring.entity.AlertStatus;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByStatusOrderByCreatedAtDesc(AlertStatus status);

    List<Alert> findAllByOrderByCreatedAtDesc();
}