package com.blogApplication.entity;

import com.blogApplication.util.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Column(unique = true, nullable = false)
    private String username;
 
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;
 
    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public Long getId() {
    	return id; }
    
    public void setId(Long id) { 
    	this.id = id; }
 
    public String getUsername() { 
    	return username; }
    
    public void setUsername(String username) { 
    	this.username = username; }
 
    public String getEmail() { 
    	return email; }
    
    public void setEmail(String email) { 
    	this.email = email; }
 
    public String getPassword() {
    	return password; }
    
    public void setPassword(String password) {
    	this.password = password; }

	public void setRole(Role user) {
		this.role= role;
	}

	public Role getRole() {
		
		return role;
	}
}