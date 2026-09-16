package com.patientmonitoring.client.controller;

import com.patientmonitoring.client.api.ApiClient;
import com.patientmonitoring.client.model.AlertItem;
import com.patientmonitoring.client.model.MeasurementItem;
import com.patientmonitoring.client.model.PatientItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

public class MainViewController {

    private final ApiClient apiClient = new ApiClient();

    @FXML
    private Label serverStatusLabel;

    @FXML
    private TableView<PatientItem> patientsTable;

    @FXML
    private TableColumn<PatientItem, Long> patientIdColumn;

    @FXML
    private TableColumn<PatientItem, String> patientNameColumn;

    @FXML
    private TableColumn<PatientItem, String> patientBirthDateColumn;

    @FXML
    private TableColumn<PatientItem, String> patientDiagnosisColumn;

    @FXML
    private TableColumn<PatientItem, String> patientPhoneColumn;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField birthDateField;

    @FXML
    private TextField diagnosisField;

    @FXML
    private TextField phoneField;

    @FXML
    private TableView<MeasurementItem> measurementsTable;

    @FXML
    private TableColumn<MeasurementItem, Long> measurementIdColumn;

    @FXML
    private TableColumn<MeasurementItem, Long> measurementPatientColumn;

    @FXML
    private TableColumn<MeasurementItem, String> measurementPressureColumn;

    @FXML
    private TableColumn<MeasurementItem, Integer> measurementPulseColumn;

    @FXML
    private TableColumn<MeasurementItem, Double> measurementTemperatureColumn;

    @FXML
    private TableColumn<MeasurementItem, Double> measurementGlucoseColumn;

    @FXML
    private TableColumn<MeasurementItem, Integer> measurementOxygenColumn;

    @FXML
    private TableColumn<MeasurementItem, String> measurementDateColumn;

    @FXML
    private TextField measurementPatientIdField;

    @FXML
    private TextField systolicPressureField;

    @FXML
    private TextField diastolicPressureField;

    @FXML
    private TextField pulseField;

    @FXML
    private TextField temperatureField;

    @FXML
    private TextField glucoseLevelField;

    @FXML
    private TextField oxygenSaturationField;

    @FXML
    private TableView<AlertItem> alertsTable;

    @FXML
    private TableColumn<AlertItem, Long> alertIdColumn;

    @FXML
    private TableColumn<AlertItem, String> alertPatientColumn;

    @FXML
    private TableColumn<AlertItem, String> alertMessageColumn;

    @FXML
    private TableColumn<AlertItem, String> alertSeverityColumn;

    @FXML
    private TableColumn<AlertItem, String> alertStatusColumn;

    @FXML
    private TableColumn<AlertItem, String> alertDateColumn;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @FXML
    private void initialize() {
        setupPatientsTable();
        setupMeasurementsTable();
        setupAlertsTable();

        patientsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedPatient) -> {
            if (selectedPatient != null) {
                measurementPatientIdField.setText(String.valueOf(selectedPatient.id()));
                loadMeasurements(selectedPatient.id());
            }
        });

        refreshAllData();
    }

    private void setupPatientsTable() {
        patientIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().id()));
        patientNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().fullName()));
        patientBirthDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().birthDate()));
        patientDiagnosisColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().diagnosis()));
        patientPhoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().phone()));
    }

    private void setupMeasurementsTable() {
        measurementIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().id()));
        measurementPatientColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().patientId()));

        measurementPressureColumn.setCellValueFactory(data -> {
            MeasurementItem item = data.getValue();
            String pressure = item.systolicPressure() + "/" + item.diastolicPressure();
            return new SimpleStringProperty(pressure);
        });

        measurementPulseColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().pulse()));
        measurementTemperatureColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().temperature()));
        measurementGlucoseColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().glucoseLevel()));
        measurementOxygenColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().oxygenSaturation()));
        measurementDateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(formatDateTime(data.getValue().measuredAt()))
        );
    }

    private void setupAlertsTable() {
        alertIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().id()));
        alertPatientColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().patientFullName()));
        alertMessageColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().message()));
        alertSeverityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().severity()));
        alertStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().status()));
        alertDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().createdAt()));
    }

    @FXML
    private void refreshAllData() {
        loadPatients();
        loadAllAlerts();
    }

    private void loadPatients() {
        try {
            List<PatientItem> patients = apiClient.getPatients();
            patientsTable.setItems(FXCollections.observableArrayList(patients));
            serverStatusLabel.setText("Сервер: online");
            serverStatusLabel.setStyle("-fx-text-fill: #22c55e;");
        } catch (Exception e) {
            serverStatusLabel.setText("Сервер: offline");
            serverStatusLabel.setStyle("-fx-text-fill: #ef4444;");
            showError("Ошибка загрузки пациентов", e.getMessage());
        }
    }

    private void loadMeasurements(Long patientId) {
        try {
            List<MeasurementItem> measurements = apiClient.getMeasurements(patientId);
            measurementsTable.setItems(FXCollections.observableArrayList(measurements));
        } catch (Exception e) {
            showError("Ошибка загрузки измерений", e.getMessage());
        }
    }

    @FXML
    private void createPatient() {
        try {
            String fullName = fullNameField.getText();

            if (fullName == null || fullName.isBlank()) {
                showError("Ошибка", "Введите ФИО пациента");
                return;
            }

            apiClient.createPatient(
                    fullNameField.getText(),
                    birthDateField.getText(),
                    diagnosisField.getText(),
                    phoneField.getText()
            );

            clearPatientForm();
            loadPatients();

            showInfo("Пациент добавлен", "Пациент успешно сохранен в базе данных");
        } catch (Exception e) {
            showError("Ошибка добавления пациента", e.getMessage());
        }
    }

    @FXML
    private void clearPatientForm() {
        fullNameField.clear();
        birthDateField.clear();
        diagnosisField.clear();
        phoneField.clear();
    }

    @FXML
    private void createMeasurement() {
        try {
            Long patientId = Long.parseLong(measurementPatientIdField.getText());

            apiClient.createMeasurement(
                    patientId,
                    Integer.parseInt(systolicPressureField.getText()),
                    Integer.parseInt(diastolicPressureField.getText()),
                    Integer.parseInt(pulseField.getText()),
                    parseDouble(temperatureField.getText()),
                    parseDouble(glucoseLevelField.getText()),
                    Integer.parseInt(oxygenSaturationField.getText())
            );

            clearMeasurementForm();
            loadMeasurements(patientId);
            loadAllAlerts();

            showInfo("Показатели сохранены", "Медицинские показатели пациента успешно добавлены");
        } catch (NumberFormatException e) {
            showError("Ошибка ввода", "Проверьте, что все числовые поля заполнены корректно");
        } catch (Exception e) {
            showError("Ошибка добавления показателей", e.getMessage());
        }
    }

    @FXML
    private void clearMeasurementForm() {
        systolicPressureField.clear();
        diastolicPressureField.clear();
        pulseField.clear();
        temperatureField.clear();
        glucoseLevelField.clear();
        oxygenSaturationField.clear();
    }

    @FXML
    private void loadAllAlerts() {
        try {
            List<AlertItem> alerts = apiClient.getAllAlerts();
            alertsTable.setItems(FXCollections.observableArrayList(alerts));
        } catch (Exception e) {
            showError("Ошибка загрузки предупреждений", e.getMessage());
        }
    }

    @FXML
    private void loadActiveAlerts() {
        try {
            List<AlertItem> alerts = apiClient.getActiveAlerts();
            alertsTable.setItems(FXCollections.observableArrayList(alerts));
        } catch (Exception e) {
            showError("Ошибка загрузки активных предупреждений", e.getMessage());
        }
    }

    @FXML
    private void resolveSelectedAlert() {
        try {
            AlertItem selectedAlert = alertsTable.getSelectionModel().getSelectedItem();

            if (selectedAlert == null) {
                showError("Ошибка", "Выберите предупреждение в таблице");
                return;
            }

            apiClient.resolveAlert(selectedAlert.id());
            loadAllAlerts();

            showInfo("Предупреждение обработано", "Статус предупреждения изменен на RESOLVED");
        } catch (Exception e) {
            showError("Ошибка обработки предупреждения", e.getMessage());
        }
    }

    private Double parseDouble(String value) {
        return Double.parseDouble(value.replace(",", "."));
    }

    private String formatDateTime(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        try {
            LocalDateTime dateTime = LocalDateTime.parse(value);
            return dateTime.format(dateFormatter);
        } catch (Exception e) {
            return value;
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}