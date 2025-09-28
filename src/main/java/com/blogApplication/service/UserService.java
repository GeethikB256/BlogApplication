package com.blogApplication.service;

import java.util.List;

import com.blogApplication.dto.LoginDto;
import com.blogApplication.dto.UserDto;
import com.blogApplication.entity.User;

public interface UserService {
	String registerUser(UserDto userDto);
    LoginDto loginUser(LoginDto loginDto);
}
