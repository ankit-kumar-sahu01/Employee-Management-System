package com.employeemanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employeemanagementsystem.config.JwtUtil;
import com.employeemanagementsystem.dto.UserLoginDTO;
import com.employeemanagementsystem.dto.UserRegistrationDTO;
import com.employeemanagementsystem.entity.Users;
import com.employeemanagementsystem.repository.UsersRepository;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UsersRepository usersRepository;
	

    @Autowired
    private PasswordEncoder passwordEncoder;
	
	//Register a new User
	@PostMapping("/register")
	public String register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
		  // Check if the user already exists
		if(usersRepository.findByUsername(userRegistrationDTO.getUsername()) != null) {
			return "Username already exist";
		}
		
		Users newUsers = new Users();
		newUsers.setUsername(userRegistrationDTO.getUsername());
		newUsers.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
		
		// Save the new user to the database
		usersRepository.save(newUsers);
		
		 // Return the JWT token in the response
		 return "Registration successful";
	}
	
	//Login and Generate a JWT token
	@PostMapping("/login")
	public String login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
		Users existingUsers = usersRepository.findByUsername(userLoginDTO.getUsername());
		
		//Verify if user exists and password matches
		if(existingUsers != null && passwordEncoder.matches(userLoginDTO.getPassword(), existingUsers.getPassword())){

			//Generate a JWT token for the new user after successful registration
			String token = jwtUtil.generateToken(existingUsers.getUsername());
			response.setHeader("Authorization", "Bearer "+ token);
			
			return "Login successfull, JWT Token: " + token;
		}else {
			return "Invalid username and password";
		}
	}
	
}
