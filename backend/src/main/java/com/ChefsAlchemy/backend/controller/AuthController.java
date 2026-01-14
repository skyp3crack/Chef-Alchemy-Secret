package com.ChefsAlchemy.backend.controller;
//this REST controller will expose the /api/auth/register endpoint.

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import com.ChefsAlchemy.backend.service.AuthService;
import com.ChefsAlchemy.backend.dto.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http:localhost:5173", maxAge = 3600)
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
}
