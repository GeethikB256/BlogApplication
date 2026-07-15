package com.blogApplication.ServiceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.blogApplication.dto.LoginDto;
import com.blogApplication.dto.UserDto;
import com.blogApplication.entity.User;
import com.blogApplication.exception.ResourceNotFoundException;
import com.blogApplication.repository.UserRepository;
import com.blogApplication.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

    private UserDto userDto;
    private User user;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "john", "john@example.com", "password123");

        user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");

        loginDto = new LoginDto("john", "password123", null);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        String result = userService.registerUser(userDto);

        assertEquals("User registered successfully!", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        String result = userService.registerUser(userDto);

        assertEquals("Username already exists!", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailExists() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(new User()));

        String result = userService.registerUser(userDto);

        assertEquals("Email already exists!", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        when(userRepository.findByUsernameOrEmail("john", "john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        LoginDto result = userService.loginUser(loginDto);

        assertEquals("Login.bru successful!", result.getMessage());
        assertEquals("john", result.getUsernameOrEmail());
    }

    @Test
    void testLoginUser_UserNotFound() {
        when(userRepository.findByUsernameOrEmail("john", "john")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.loginUser(loginDto);
        });

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testLoginUser_InvalidPassword() {
        when(userRepository.findByUsernameOrEmail("john", "john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        LoginDto invalidLoginDto = new LoginDto("john", "wrongPassword", null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.loginUser(invalidLoginDto);
        });

        assertEquals("Invalid password!", exception.getMessage());
    }
}