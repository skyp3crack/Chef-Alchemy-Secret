package com.ChefsAlchemy.backend.service;

//contain business logic for user registerrations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ChefsAlchemy.backend.dto.RegisterRequest;
import com.ChefsAlchemy.backend.repository.UserRepository;
import com.ChefsAlchemy.backend.model.User;
import com.ChefsAlchemy.backend.dto.LoginRequest;
import com.ChefsAlchemy.backend.security.jwt.JwtUtils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Service
// this is class
public class AuthService {
    @Autowired // autowire is for dependency injection
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager; // Inject AuthenticationManager

    // this method is for registering a new user
    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("error: username already taken");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("error: email already taken");
        }
        // create new user's account
        // below is constructor
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()));
        // set default role in user constuctor , but could addd more here if needed
        userRepository.save(user); // save user to database
    }

    // this method is for authenticating the user and generating a JWT token
    public String authenticateUserGenerateJwt(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()) // this is for authenticating the user
        );
        SecurityContextHolder.getContext().setAuthentication(authentication); // set the authentication in the security
                                                                              // context
        return jwtUtils.generateJwtToken(authentication); // generate the JWT token
    }

    // method to get user details from authentication
    public UserDetails getUserDetailsFromAuthentication(Authentication authentication) {
        return (UserDetails) authentication.getPrincipal();
    }

    public User getUserFromUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("error: user not found"));
    }
}
