package com.blogApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogApplication.dto.JwtResponse;
import com.blogApplication.dto.LoginDto;
import com.blogApplication.dto.UserDto;
import com.blogApplication.security.CustomUserDetails;
import com.blogApplication.security.JwtUtil;
import com.blogApplication.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
 
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
 
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto dto) {
        String result = userService.registerUser(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()
                )
        );
       
        String token = jwtUtil.generateToken(loginDto.getUsernameOrEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}