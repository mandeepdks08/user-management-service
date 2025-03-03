package com.users.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.users.datamodel.User;
import com.users.repository.UserRepository;
import com.users.restmodel.LoginRequest;
import com.users.restmodel.LoginResponse;
import com.users.util.JwtUtil;

@Component
public class UserLoginHandler {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwtUtil;

	public LoginResponse login(LoginRequest loginRequest) throws Exception {
		User user = userRepo.findByEmail(loginRequest.getEmail());
		if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
			throw new Exception("Invalid credentials!");
		}
		String jwtToken = jwtUtil.generateToken(user);
		return LoginResponse.builder().jwtToken(jwtToken).build();
	}
}
