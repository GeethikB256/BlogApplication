package com.blogApplication.ServiceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.blogApplication.dto.LoginDto;
import com.blogApplication.dto.UserDto;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterUser_Success() {
        UserDto userDto = new UserDto(1L, "john", "john@example.com", "password123");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        String result = userService.registerUser(userDto);

        assertEquals("User registered successfully!", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        UserDto userDto = new UserDto(1L, "john", "john@example.com", "password123");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        String result = userService.registerUser(userDto);

        assertEquals("Username already exists!", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailExists() {
        UserDto userDto = new UserDto(1L, "john", "john@example.com", "password123");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(new User()));

        String result = userService.registerUser(userDto);

        assertEquals("Email already exists!", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsernameOrEmail("john", "john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        LoginDto loginDto = new LoginDto("john", "password123", null);
        LoginDto result = userService.loginUser(loginDto);

        assertEquals("Login successful!", result.getMessage());
        assertEquals("john", result.getUsernameOrEmail());
    }

    @Test
    void testLoginUser_UserNotFound() {
        when(userRepository.findByUsernameOrEmail("john", "john")).thenReturn(Optional.empty());

        LoginDto loginDto = new LoginDto("john", "password123", null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.loginUser(loginDto);
        });

        assertTrue(exception.getMessage().contains("User not found"));
    }
    
    @Test
    void testLoginUser_InvalidPassword() {
        User user = new User();
        user.setPassword("encodedPassword");

        when(userRepository.findByUsernameOrEmail("john", "john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        LoginDto loginDto = new LoginDto("john", "wrongPassword", null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.loginUser(loginDto);
        });

        assertEquals("Invalid password!", exception.getMessage());
    }
}