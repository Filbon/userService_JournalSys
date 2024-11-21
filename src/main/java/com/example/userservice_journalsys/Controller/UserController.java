package com.example.userservice_journalsys.Controller;

import com.example.userservice_journalsys.DTO.UserDTO;
import com.example.userservice_journalsys.Model.User;
import com.example.userservice_journalsys.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(@Lazy UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        logger.info("Starting user registration for username: {}", userDTO.getUserName());

        try {
            UserDTO createdUser = userService.registerUser(userDTO);
            logger.info("User registration successful for username: {}", userDTO.getUserName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            logger.error("User registration failed for username: {}", userDTO.getUserName(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        User user = userService.authenticate(userDTO);
        if (user != null) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("role", user.getRole().name());
            response.put("userId", user.getId().toString());
            response.put("userName", user.getUserName());
            if(user.getRole().name().equals("PATIENT")){
                String patientId = userService.getPatientIdByUserId(user.getId()).toString();
                response.put("patientId", patientId);
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);

        try {
            UserDTO userDTO = userService.getUserById(id);
            System.out.println(userDTO.getUserName());
            logger.info("User found with ID: {}", id);
            return ResponseEntity.ok(userDTO);
        } catch (EntityNotFoundException e) {
            logger.error("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/users/doctorOrStaff")
    public ResponseEntity<List<UserDTO>> getDoctorsAndStaff() {
        List<UserDTO> userDTOs = userService.getDoctorsAndStaff();
        return ResponseEntity.ok(userDTOs);
    }
}
