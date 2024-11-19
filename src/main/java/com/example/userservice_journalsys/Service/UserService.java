package com.example.userservice_journalsys.Service;

import com.example.userservice_journalsys.DTO.PatientDTO;
import com.example.userservice_journalsys.DTO.PractitionerDTO;
import com.example.userservice_journalsys.DTO.UserDTO;
import com.example.userservice_journalsys.Model.Role;
import com.example.userservice_journalsys.Model.User;
import com.example.userservice_journalsys.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate; // For inter-service communication if needed

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RestTemplate restTemplate)  {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public UserDTO registerUser(UserDTO userDTO) {
        // Check if the username already exists in the database
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new RuntimeException("Username is already taken");
        }

        // Convert the DTO to a User entity
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // Encode password
        user.setRole(userDTO.getRole()); // Set role from UserDTO

        // For roles other than PATIENT, we might communicate with another service (e.g., Practitioner Service)
        if (userDTO.getRole() == Role.PATIENT) {
            // Create a new Patient record
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setName(userDTO.getUserName());
            patientDTO.setUserId(user.getId());

            // Call Patient Service to create a new Patient record
            restTemplate.postForObject("http://patient-service/create", patientDTO, PatientDTO.class);
        } else if (userDTO.getRole() == Role.DOCTOR || userDTO.getRole() == Role.STAFF) {
            // Create a new Practitioner record
            PractitionerDTO practitionerDTO = new PractitionerDTO();
            practitionerDTO.setName(userDTO.getUserName());
            practitionerDTO.setRole(userDTO.getRole());

            // Call Practitioner Service to create a new Practitioner record
            restTemplate.postForObject("http://practitioner-service/create", practitionerDTO, PractitionerDTO.class);
        }

        // Save the user in the database
        User savedUser = userRepository.save(user);

        // Convert the saved user back to UserDTO to return
        return modelMapper.map(savedUser, UserDTO.class);
    }

    // Helper method to convert User to UserDTO
    private UserDTO convertUserToDTO(User user) {

        return modelMapper.map(user, UserDTO.class);
    }

    // Fetch all users
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertUserToDTO) // Convert each User to UserDTO
                .collect(Collectors.toList());
    }

    // Fetch a single user by ID
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return convertUserToDTO(user);
    }

    // Create a new user
    public UserDTO createUser(UserDTO userDTO) {
        // Hash the password before saving the user
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = modelMapper.map(userDTO, User.class); // Convert UserDTO to User entity
        user.setPassword(hashedPassword); // Set the hashed password

        User savedUser = userRepository.save(user); // Save user to the database
        return convertUserToDTO(savedUser); // Return the saved user as a DTO
    }

    // Delete a user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id); // Delete user from the repository
    }

    // Authentication method
    public User authenticate(UserDTO userDTO) {
        User user = userRepository.findByUserName(userDTO.getUserName());

        if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return user; // Authentication successful
        }

        throw new RuntimeException("Invalid credentials");
    }
}

