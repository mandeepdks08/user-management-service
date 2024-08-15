package com.convo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.convo.datamodel.User;
import com.convo.repository.UserRepository;
import com.convo.restmodel.UserRegisterRequest;

@Component
public class UserRegistrationHandler {

	@Autowired
	private UserRepository userRepo;

	public void register(UserRegisterRequest request) {
		validateRegistrationRequest(request);
		User user = User.builder().name(request.getName()).email(request.getEmail()).phone(request.getPhone()).build();
		userRepo.save(user);
	}

	private void validateRegistrationRequest(UserRegisterRequest request) {

	}
}
