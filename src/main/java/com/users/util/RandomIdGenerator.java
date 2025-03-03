package com.users.util;

public class RandomIdGenerator {

	private static final String ALPHANUMERIC_CHARACTERS = "0123456789";

	public static String generateRandomId(int length) {
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = ((int)(Math.random() * 100) % ALPHANUMERIC_CHARACTERS.length());
			while (i == 0 && index == 0) {
				index = ((int)(Math.random() * 100) % ALPHANUMERIC_CHARACTERS.length());
			}
			sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
		}

		return sb.toString();
	}
}