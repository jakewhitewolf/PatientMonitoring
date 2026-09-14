package ru.patientmonitoring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.patientmonitoring.dto.PatientCreateRequest;
import ru.patientmonitoring.dto.PatientResponse;
import ru.patientmonitoring.entity.Patient;
import ru.patientmonitoring.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void createPatient_shouldSavePatientAndReturnResponse() {
        PatientCreateRequest request = new PatientCreateRequest();
        request.setFullName("Иванов Иван Иванович");
        request.setBirthDate(LocalDate.of(1975, 4, 12));
        request.setDiagnosis("Сахарный диабет 2 типа");
        request.setPhone("+79990000000");

        Patient savedPatient = new Patient();
        savedPatient.setId(1L);
        savedPatient.setFullName("Иванов Иван Иванович");
        savedPatient.setBirthDate(LocalDate.of(1975, 4, 12));
        savedPatient.setDiagnosis("Сахарный диабет 2 типа");
        savedPatient.setPhone("+79990000000");
        savedPatient.setCreatedAt(LocalDateTime.now());

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        PatientResponse response = patientService.createPatient(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Иванов Иван Иванович", response.getFullName());
        assertEquals("Сахарный диабет 2 типа", response.getDiagnosis());
        assertEquals("+79990000000", response.getPhone());
    }
}
