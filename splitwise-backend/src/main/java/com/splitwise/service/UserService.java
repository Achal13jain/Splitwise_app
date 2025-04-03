package com.splitwise.service;

import com.splitwise.dto.LoginRequest;
import com.splitwise.dto.UserRequest;
import com.splitwise.model.User;
import com.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    public String register(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return "User already exists!";
        }
        if (userRepository.findByName(request.getName()) != null) {
            return "Username not available!";
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Always save new users with encrypted password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "User registered successfully!";
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) return null;

        // Match both hashed and plain-text
        if (passwordEncoder.matches(request.getPassword(), user.getPassword()) ||
                user.getPassword().equals(request.getPassword())) {
            return user;
        }

        return null;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
