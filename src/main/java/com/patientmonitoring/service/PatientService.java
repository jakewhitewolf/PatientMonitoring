package ru.patientmonitoring.service;

import org.springframework.stereotype.Service;
import ru.patientmonitoring.dto.PatientCreateRequest;
import ru.patientmonitoring.dto.PatientResponse;
import ru.patientmonitoring.entity.Patient;
import ru.patientmonitoring.repository.PatientRepository;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientResponse createPatient(PatientCreateRequest request) {
        Patient patient = new Patient(
                request.getFullName(),
                request.getBirthDate(),
                request.getDiagnosis(),
                request.getPhone()
        );

        Patient savedPatient = patientRepository.save(patient);

        return toResponse(savedPatient);
    }

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пациент не найден"));

        return toResponse(patient);
    }

    private PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFullName(),
                patient.getBirthDate(),
                patient.getDiagnosis(),
                patient.getPhone(),
                patient.getCreatedAt()
        );
    }
}
