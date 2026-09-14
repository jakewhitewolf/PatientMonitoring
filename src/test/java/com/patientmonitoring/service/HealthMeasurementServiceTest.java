package ru.patientmonitoring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.patientmonitoring.dto.HealthMeasurementCreateRequest;
import ru.patientmonitoring.dto.HealthMeasurementResponse;
import ru.patientmonitoring.entity.Alert;
import ru.patientmonitoring.entity.AlertSeverity;
import ru.patientmonitoring.entity.HealthMeasurement;
import ru.patientmonitoring.entity.Patient;
import ru.patientmonitoring.repository.AlertRepository;
import ru.patientmonitoring.repository.HealthMeasurementRepository;
import ru.patientmonitoring.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthMeasurementServiceTest {

    @Mock
    private HealthMeasurementRepository healthMeasurementRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private HealthMeasurementService healthMeasurementService;

    @Test
    void createMeasurement_shouldNotCreateAlert_whenIndicatorsAreNormal() {
        Patient patient = createTestPatient();

        HealthMeasurementCreateRequest request = new HealthMeasurementCreateRequest();
        request.setSystolicPressure(120);
        request.setDiastolicPressure(80);
        request.setPulse(75);
        request.setTemperature(36.6);
        request.setGlucoseLevel(5.2);
        request.setOxygenSaturation(98);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(healthMeasurementRepository.save(any(HealthMeasurement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HealthMeasurementResponse response = healthMeasurementService.createMeasurement(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getPatientId());
        assertEquals(120, response.getSystolicPressure());
        assertEquals(80, response.getDiastolicPressure());
        assertEquals(75, response.getPulse());
        assertEquals(36.6, response.getTemperature());
        assertEquals(5.2, response.getGlucoseLevel());
        assertEquals(98, response.getOxygenSaturation());

        verify(alertRepository, never()).save(any(Alert.class));
    }

    @Test
    void createMeasurement_shouldCreateCriticalAlert_whenIndicatorsAreBad() {
        Patient patient = createTestPatient();

        HealthMeasurementCreateRequest request = new HealthMeasurementCreateRequest();
        request.setSystolicPressure(170);
        request.setDiastolicPressure(105);
        request.setPulse(135);
        request.setTemperature(39.2);
        request.setGlucoseLevel(14.5);
        request.setOxygenSaturation(88);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(healthMeasurementRepository.save(any(HealthMeasurement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(alertRepository.save(any(Alert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        healthMeasurementService.createMeasurement(1L, request);

        ArgumentCaptor<Alert> alertCaptor = ArgumentCaptor.forClass(Alert.class);

        verify(alertRepository, times(1)).save(alertCaptor.capture());

        Alert savedAlert = alertCaptor.getValue();

        assertNotNull(savedAlert);
        assertEquals(patient, savedAlert.getPatient());
        assertEquals(AlertSeverity.CRITICAL, savedAlert.getSeverity());
        assertTrue(savedAlert.getMessage().contains("Иванов Иван Иванович"));
        assertTrue(savedAlert.getMessage().contains("критическое отклонение"));
    }

    private Patient createTestPatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Иванов Иван Иванович");
        patient.setBirthDate(LocalDate.of(1975, 4, 12));
        patient.setDiagnosis("Сахарный диабет 2 типа");
        patient.setPhone("+79990000000");
        patient.setCreatedAt(LocalDateTime.now());
        return patient;
    }
}