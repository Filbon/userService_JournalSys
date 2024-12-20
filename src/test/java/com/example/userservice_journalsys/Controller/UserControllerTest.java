package com.example.userservice_journalsys.Controller;

import com.example.userservice_journalsys.DTO.UserDTO;
import com.example.userservice_journalsys.Model.Role;
import com.example.userservice_journalsys.Model.User;
import com.example.userservice_journalsys.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testRegisterUser_success() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("testUser");
        userDTO.setPassword("password");
        userDTO.setRole(Role.PATIENT);

        UserDTO createdUser = new UserDTO();
        createdUser.setUserName("testUser");
        createdUser.setRole(Role.PATIENT);

        when(userService.registerUser(any(UserDTO.class))).thenReturn(createdUser);

        // Act & Assert
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("testUser"))
                .andExpect(jsonPath("$.role").value("PATIENT"));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void testRegisterUser_failure() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("testUser");

        when(userService.registerUser(any(UserDTO.class))).thenThrow(new RuntimeException("Registration failed"));

        // Act & Assert
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void testLogin_success() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("testUser");
        userDTO.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUserName("testUser");
        user.setRole(Role.PATIENT);

        when(userService.authenticate(any(UserDTO.class))).thenReturn(user);
        when(userService.getUserRoleIdByUserId(1L)).thenReturn(100L);

        // Act & Assert
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.patientId").value("100"));

        verify(userService, times(1)).authenticate(any(UserDTO.class));
        verify(userService, times(1)).getUserRoleIdByUserId(1L);
    }

    @Test
    void testLogin_failure() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("testUser");
        userDTO.setPassword("wrongPassword");

        when(userService.authenticate(any(UserDTO.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));

        verify(userService, times(1)).authenticate(any(UserDTO.class));
    }

    @Test
    void testGetUserById_success() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUserName("testUser");

        when(userService.getUserById(1L)).thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(get("/api/user/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testUser"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_notFound() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        mockMvc.perform(get("/api/user/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetDoctorsAndStaff() throws Exception {
        // Arrange
        UserDTO doctor = new UserDTO();
        doctor.setUserName("doctorUser");
        doctor.setRole(Role.DOCTOR);

        UserDTO staff = new UserDTO();
        staff.setUserName("staffUser");
        staff.setRole(Role.STAFF);

        List<UserDTO> users = Arrays.asList(doctor, staff);

        when(userService.getDoctorsAndStaff()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/user/users/doctorOrStaff")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userName").value("doctorUser"))
                .andExpect(jsonPath("$[1].userName").value("staffUser"));

        verify(userService, times(1)).getDoctorsAndStaff();
    }
}

