package com.example.userservice_journalsys.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class PatientDTO {

    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("birthdate")
    private LocalDate birthdate;
    @JsonProperty("userId")
    private Long userId;  // Instead of embedding the entire User object

    @JsonProperty("conditions")
    private List<ConditionDTO> conditions; // Instead of embedding ConditionDTO, store condition IDs
    private List<Long> encounterIds; // Store encounter IDs
    private List<Long> observationIds; // Store observation IDs (IDs referencing Observation entities)

    // Constructors
    public PatientDTO(Long id, String name, LocalDate birthdate, Long userId, List<ConditionDTO> conditions, List<Long> encounterIds, List<Long> observationIds) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.userId = userId;
        this.conditions = conditions;
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

    public List<ConditionDTO> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionDTO> conditions) {
        this.conditions = conditions;
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

