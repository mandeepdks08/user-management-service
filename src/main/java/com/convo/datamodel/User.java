package com.convo.datamodel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class User extends DbBaseModel {
	private String userId;
	private String name;
	private String email;
	private String phone;

	public User basicInfo() {
		return User.builder().userId(userId).name(name).build();
	}
}
