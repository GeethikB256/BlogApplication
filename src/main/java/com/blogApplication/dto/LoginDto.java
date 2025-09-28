package com.blogApplication.dto;

public class LoginDto {
	private String usernameOrEmail;
    private String password;
    private String message;

    public LoginDto() {
    }
    public LoginDto(String usernameOrEmail,String password,String message){
        this.usernameOrEmail=usernameOrEmail;
        this.password=password;
        this.message=message;
    }
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}


