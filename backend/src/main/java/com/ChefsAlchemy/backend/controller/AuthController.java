package com.ChefsAlchemy.backend.controller;
//this REST controller will expose the /api/auth/register endpoint.

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import com.ChefsAlchemy.backend.service.AuthService;
import com.ChefsAlchemy.backend.dto.JwtResponse;
import com.ChefsAlchemy.backend.dto.LoginRequest;
import com.ChefsAlchemy.backend.dto.RegisterRequest;
import com.ChefsAlchemy.backend.model.User;

import java.util.stream.Collectors;
import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")

public class AuthController {
    @Autowired
    private AuthService authService; // is used to inject the AuthService dependency

    // post request to /api/auth/register
    @PostMapping("/register") // @Valid is used to validate the request body @NotBlank and @Size
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // call service to save the user to database
            authService.registerUser(registerRequest);
            return new ResponseEntity<>("User Registered successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String jwt = authService.authenticateUserGenerateJwt(loginRequest); // generate JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // get
                                                                                                    // authentication
                                                                                                    // from security
                                                                                                    // context

            UserDetails userDetails = authService.getUserDetailsFromAuthentication(authentication); // get user details
            User user = authService.getUserFromUsername(userDetails.getUsername()); // get user from username

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList()); // get roles

            return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles));
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid credentials: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
