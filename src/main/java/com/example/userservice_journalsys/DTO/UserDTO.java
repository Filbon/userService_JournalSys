package com.example.userservice_journalsys.DTO;

import com.example.userservice_journalsys.Model.Role;

public class UserDTO {

    private Long id;
    private String userName;
    private Role role;
    private String password;

    private PatientDTO patient;

    // Constructor to initialize all fields
    public UserDTO(Long id, String userName, Role role, String password, PatientDTO patient) {
        this.id = id;
        this.userName = userName;
        this.role = role;
        this.password = password;
        this.patient = patient;
    }
    public UserDTO() {

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }
}
