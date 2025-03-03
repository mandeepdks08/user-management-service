package com.users.handler;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.users.datamodel.User;
import com.users.repository.UserRepository;

@Component
public class UserUpdateHandler {

	@Autowired
	private UserRepository userRepo;

	public void updateUser(User user) throws Exception {
		User updatedUser = getUpdatedUser(user);
		userRepo.save(updatedUser);
	}

	private User getUpdatedUser(User user) throws Exception {
		if (user.getId() == null) {
			throw new Exception("User does not exist");
		}
		User dbUser = userRepo.findById(user.getId()).orElse(null);
		if (dbUser == null) {
			throw new Exception("User does not exist");
		}
		Field[] fields = user.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.get(user) != null) {
					field.set(dbUser, field.get(user));
				}
			} catch (Exception e) {

			}
		}
		return dbUser;
	}
}
