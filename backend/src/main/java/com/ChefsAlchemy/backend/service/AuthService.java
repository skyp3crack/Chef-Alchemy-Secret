package com.ChefsAlchemy.backend.service;

//contain business logic for user registerrations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ChefsAlchemy.backend.dto.RegisterRequest;
import com.ChefsAlchemy.backend.repository.UserRepository;
import com.ChefsAlchemy.backend.model.User;

@Service
// this is class
public class AuthService {
    @Autowired // autowire is for dependency injection
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // this method is for registering a new user
    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsbyUsername(registerRequest.getUsername())) {
            throw new RuntimeException("error: username already taken");
        }
        if (userRepository.existsbyEmail(registerRequest.getEmail())) {
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
}
