package com.example.userservice_journalsys.DTO;

import com.example.userservice_journalsys.Model.Role;

public class PractitionerDTO {

    private Long id;
    private String name;
    private Role role;
    private Long organizationId;  // Organization that the practitioner is part of
    private Long userId;  // Link back to the associated User

    // Constructor
    public PractitionerDTO(Long id, String name, Role role, Long organizationId, Long userId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.organizationId = organizationId;
        this.userId = userId;
    }

    public PractitionerDTO() {

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

