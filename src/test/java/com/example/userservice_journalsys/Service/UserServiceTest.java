package com.example.userservice_journalsys.Service;

import com.example.userservice_journalsys.DTO.PatientDTO;
import com.example.userservice_journalsys.DTO.UserDTO;
import com.example.userservice_journalsys.Model.Role;
import com.example.userservice_journalsys.Model.User;
import com.example.userservice_journalsys.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_whenUsernameExists_shouldThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("existingUser");

        when(userRepository.existsByUserName("existingUser")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userDTO));
        assertEquals("Username is already taken", exception.getMessage());
    }

    @Test
    void testRegisterUser_whenRoleIsPatient_shouldCreatePatient() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("newUser");
        userDTO.setPassword("password");
        userDTO.setRole(Role.PATIENT);

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setBirthdate(LocalDate.of(1990, 1, 1)); // Set a valid birthdate
        userDTO.setPatient(patientDTO); // Set the patient field in UserDTO

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUserName("newUser");

        UserDTO mappedUserDTO = new UserDTO();
        mappedUserDTO.setUserName("newUser");
        mappedUserDTO.setRole(Role.PATIENT);

        when(userRepository.existsByUserName("newUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(PatientDTO.class)))
                .thenReturn(null); // Simulate the response from the POST request
        when(modelMapper.map(savedUser, UserDTO.class)).thenReturn(mappedUserDTO);

        // Act
        UserDTO returnedUser = userService.registerUser(userDTO);

        // Assert
        assertNotNull(returnedUser); // Ensure the returned object is not null
        assertEquals("newUser", returnedUser.getUserName());
        verify(restTemplate, times(1)).postForObject(
                eq("https://userroleservice.app.cloud.cbh.kth.se/api/userRole/patient/create"),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        );
    }




    @Test
    void testGetAllUsers_shouldReturnUserDTOList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUserName("user2");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUserName(user.getUserName());
            return dto;
        });

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUserName());
        assertEquals("user2", result.get(1).getUserName());
    }

    @Test
    void testGetUserById_whenUserExists_shouldReturnUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setUserName("user1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(new UserDTO(1L, "user1", null, null, null));

        UserDTO result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        assertEquals("user1", result.getUserName());
    }

    @Test
    void testGetUserById_whenUserDoesNotExist_shouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testAuthenticate_whenCredentialsAreValid_shouldReturnUser() {
        User user = new User();
        user.setUserName("user1");
        user.setPassword("encodedPassword");

        when(userRepository.findByUserName("user1")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User result = userService.authenticate(new UserDTO(null, "user1", null, "password", null));

        assertEquals("user1", result.getUserName());
    }

    @Test
    void testAuthenticate_whenCredentialsAreInvalid_shouldThrowException() {
        when(userRepository.findByUserName("user1")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.authenticate(new UserDTO(null, "user1", null, "password", null)));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testDeleteUser_whenUserExists_shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_whenUserDoesNotExist_shouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found", exception.getMessage());
    }
}
