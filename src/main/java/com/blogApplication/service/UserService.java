package com.blogApplication.service;

import com.blogApplication.dto.LoginDto;
import com.blogApplication.dto.UserDto;

public interface UserService {
	String registerUser(UserDto userDto);
    LoginDto loginUser(LoginDto loginDto);
}
