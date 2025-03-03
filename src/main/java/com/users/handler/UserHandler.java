package com.users.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.users.datamodel.User;
import com.users.repository.UserRepository;
import com.users.restmodel.UserRegisterRequest;
import com.users.security.JwtAuthenticationFilter;
import com.users.util.JwtUtil;
import com.users.util.RandomIdGenerator;

@Component
public class UserHandler {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;

	@Autowired
	private JwtUtil jwtUtil;

	public void register(UserRegisterRequest request) {
		validateRegistrationRequest(request);
		String userId = RandomIdGenerator.generateRandomId(10);
		System.out.println(userId);
		User user = User.builder().userId(userId).name(request.getName()).email(request.getEmail())
				.password(request.getPassword()).phone(request.getPhone()).createdOn(LocalDateTime.now()).enabled(true)
				.processedOn(LocalDateTime.now()).build();
		userRepo.save(user);
	}

	public List<User> listUsers(List<String> userIds) {
		return userRepo.findByUserIdIn(ObjectUtils.firstNonNull(userIds, new ArrayList<>()));
	}

	public User authenticate(String token) throws Exception {
		try {
			if (jwtAuthFilter.authenticate(token) == true) {
				String email = jwtUtil.extractUsername(token.substring(7));
				return userRepo.findByEmail(email);
			} else {
				throw new Exception("Authentication failed!");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void validateRegistrationRequest(UserRegisterRequest request) {

	}

}
