package com.example.userservice_journalsys.DTO;

import java.time.LocalDate;
import java.util.List;

public class PatientDTO {
    private Long id;
    private String name;
    private LocalDate birthdate;
    private Long userId;  // Instead of embedding the entire User object
    private List<Long> conditionIds; // Instead of embedding ConditionDTO, store condition IDs
    private List<Long> encounterIds; // Store encounter IDs
    private List<Long> observationIds; // Store observation IDs (IDs referencing Observation entities)

    // Constructors
    public PatientDTO(Long id, String name, LocalDate birthdate, Long userId, List<Long> conditionIds, List<Long> encounterIds, List<Long> observationIds) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.userId = userId;
        this.conditionIds = conditionIds;
        this.encounterIds = encounterIds;
        this.observationIds = observationIds;
    }

    public PatientDTO() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getConditionIds() {
        return conditionIds;
    }

    public void setConditionIds(List<Long> conditionIds) {
        this.conditionIds = conditionIds;
    }

    public List<Long> getEncounterIds() {
        return encounterIds;
    }

    public void setEncounterIds(List<Long> encounterIds) {
        this.encounterIds = encounterIds;
    }

    public List<Long> getObservationIds() {
        return observationIds;
    }

    public void setObservationIds(List<Long> observationIds) {
        this.observationIds = observationIds;
    }
}

