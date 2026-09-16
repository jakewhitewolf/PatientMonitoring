package com.patientmonitoring.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patientmonitoring.client.model.AlertItem;
import com.patientmonitoring.client.model.MeasurementItem;
import com.patientmonitoring.client.model.PatientItem;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PatientItem> getPatients() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/patients"))
                .GET()
                .build();

        String responseBody = sendRequest(request);
        PatientItem[] patients = objectMapper.readValue(responseBody, PatientItem[].class);

        return Arrays.asList(patients);
    }

    public PatientItem createPatient(String fullName, String birthDate, String diagnosis, String phone)
            throws IOException, InterruptedException {

        Map<String, Object> body = Map.of(
                "fullName", fullName,
                "birthDate", emptyToNull(birthDate),
                "diagnosis", emptyToNull(diagnosis),
                "phone", emptyToNull(phone)
        );

        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/patients"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        String responseBody = sendRequest(request);

        return objectMapper.readValue(responseBody, PatientItem.class);
    }

    public List<MeasurementItem> getMeasurements(Long patientId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/patients/" + patientId + "/measurements"))
                .GET()
                .build();

        String responseBody = sendRequest(request);
        MeasurementItem[] measurements = objectMapper.readValue(responseBody, MeasurementItem[].class);

        return Arrays.asList(measurements);
    }

    public MeasurementItem createMeasurement(Long patientId,
                                             Integer systolicPressure,
                                             Integer diastolicPressure,
                                             Integer pulse,
                                             Double temperature,
                                             Double glucoseLevel,
                                             Integer oxygenSaturation)
            throws IOException, InterruptedException {

        Map<String, Object> body = Map.of(
                "systolicPressure", systolicPressure,
                "diastolicPressure", diastolicPressure,
                "pulse", pulse,
                "temperature", temperature,
                "glucoseLevel", glucoseLevel,
                "oxygenSaturation", oxygenSaturation
        );

        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/patients/" + patientId + "/measurements"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        String responseBody = sendRequest(request);

        return objectMapper.readValue(responseBody, MeasurementItem.class);
    }

    public List<AlertItem> getAllAlerts() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/alerts"))
                .GET()
                .build();

        String responseBody = sendRequest(request);
        AlertItem[] alerts = objectMapper.readValue(responseBody, AlertItem[].class);

        return Arrays.asList(alerts);
    }

    public List<AlertItem> getActiveAlerts() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/alerts/active"))
                .GET()
                .build();

        String responseBody = sendRequest(request);
        AlertItem[] alerts = objectMapper.readValue(responseBody, AlertItem[].class);

        return Arrays.asList(alerts);
    }

    public AlertItem resolveAlert(Long alertId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/alerts/" + alertId + "/resolve"))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        String responseBody = sendRequest(request);

        return objectMapper.readValue(responseBody, AlertItem.class);
    }

    private String sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Ошибка запроса. Код: " + response.statusCode() + ". Ответ: " + response.body());
        }

        return response.body();
    }

    private Object emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}