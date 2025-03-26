
package com.splitwise.service;

import com.splitwise.model.User;
import com.splitwise.repository.UserRepository;
import com.splitwise.dto.UserRequest;
import com.splitwise.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public String register(UserRequest request) {
//        if (userRepository.findByEmail(request.getEmail()) != null) {
//            return "User already exists!";
//        }
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPassword(request.getPassword());
//        userRepository.save(user);
//        return "User registered successfully!";
//    }
//
//    public String login(LoginRequest request) {
//        User user = userRepository.findByEmail(request.getEmail());
//        if (user == null || !user.getPassword().equals(request.getPassword())) {
//            return "Invalid credentials!";
//        }
//        return "Login successful!";
//    }
//}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    public String register(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return "User already exists!";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // 🔐 Encrypt password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return "User registered successfully!";
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return "Invalid credentials!";
        }

        // 🔐 Compare encoded password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Invalid credentials!";
        }

        return "Login successful!";
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
