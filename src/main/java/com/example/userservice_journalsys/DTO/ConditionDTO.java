package com.example.userservice_journalsys.DTO;

import java.time.LocalDate;

public class ConditionDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate diagnosisDate;
    private Long patientId;

    private ConditionStatus status;

    // Constructor
    public ConditionDTO(Long id, String conditionName, String description, LocalDate diagnosisDate, Long patientId, ConditionStatus status) {
        this.id = id;
        this.name = conditionName;
        this.description = description;
        this.diagnosisDate = diagnosisDate;
        this.patientId = patientId;
        this.status = status;
    }

    public ConditionDTO() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionName() {
        return name;
    }

    public void setConditionName(String conditionName) {
        this.name = conditionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public ConditionStatus getStatus() {
        return status;
    }

    public void setStatus(ConditionStatus status) {
        this.status = status;
    }
}
