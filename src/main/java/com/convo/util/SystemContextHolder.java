package com.convo.util;

import com.convo.datamodel.User;

public class SystemContextHolder {
	private static ThreadLocal<User> loggedInUser;

	public static void setLoggedInUser(User loggedInUser) {
		SystemContextHolder.loggedInUser = new ThreadLocal<>();
		SystemContextHolder.loggedInUser.set(loggedInUser);
	}

	public static User getLoggedInUser() {
		return loggedInUser != null ? loggedInUser.get() : null;
	}
}
