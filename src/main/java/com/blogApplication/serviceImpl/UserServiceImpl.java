package com.blogApplication.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogApplication.dto.LoginDto;
import com.blogApplication.dto.UserDto;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.service.UserService;
import com.blogApplication.util.Role;

@Service
public class UserServiceImpl implements UserService {
 
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    @Override
    public String registerUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return "Username already exists!";
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER); // Default role
 
        userRepository.save(user);
        return "User registered successfully!";
    }
 
    @Override
    public LoginDto loginUser(LoginDto loginDto) {
        Optional<User> userOpt =
                userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());
 
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with: " + loginDto.getUsernameOrEmail());
        }
 
        User user = userOpt.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid password!");
        }
        LoginDto response = new LoginDto();
        response.setUsernameOrEmail(user.getUsername());
        return response;
    }
 

}