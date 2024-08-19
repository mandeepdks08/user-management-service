package com.convo.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.convo.datamodel.User;
import com.convo.repository.UserRepository;
import com.convo.restmodel.UserRegisterRequest;
import com.convo.util.RandomIdGenerator;

@Component
public class UserHandler {

	@Autowired
	private UserRepository userRepo;

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

	private void validateRegistrationRequest(UserRegisterRequest request) {

	}

}
