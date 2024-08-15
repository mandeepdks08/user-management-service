package com.convo.util;

import java.security.SecureRandom;

public class RandomIdGenerator {

	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();

	public static String generateRandomId(int length) {
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length());
			sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
		}

		return sb.toString();
	}
}