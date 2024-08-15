package com.convo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.convo.datamodel.User;
import com.convo.repository.UserRepository;
import com.convo.restmodel.LoginRequest;
import com.convo.restmodel.LoginResponse;
import com.convo.util.JwtUtil;

@Component
public class UserLoginHandler {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwtUtil;

	public LoginResponse login(LoginRequest loginRequest) {
		User user = userRepo.findByEmail(loginRequest.getEmail());
		String jwtToken = jwtUtil.generateToken(user);
		return LoginResponse.builder().jwtToken(jwtToken).build();
	}
}
