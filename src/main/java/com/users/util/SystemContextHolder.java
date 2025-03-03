package com.users.util;

import javax.servlet.http.HttpServletRequest;

import com.users.datamodel.User;

public class SystemContextHolder {
	private static ThreadLocal<User> loggedInUser;
	private static ThreadLocal<HttpServletRequest> httpRequest;

	public static void setLoggedInUser(User loggedInUser) {
		if (SystemContextHolder.loggedInUser == null) {
			SystemContextHolder.loggedInUser = new ThreadLocal<>();
		}
		SystemContextHolder.loggedInUser.set(loggedInUser);
	}

	public static User getLoggedInUser() {
		return loggedInUser != null ? loggedInUser.get() : null;
	}

	public static void setHttpRequest(HttpServletRequest request) {
		if (SystemContextHolder.httpRequest == null) {
			SystemContextHolder.httpRequest = new ThreadLocal<>();
		}
		SystemContextHolder.httpRequest.set(request);
	}

	public static HttpServletRequest getHttpRequest() {
		return httpRequest != null ? httpRequest.get() : null;
	}
}
