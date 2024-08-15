package com.convo.restmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegisterRequest {
	private String name;
	private String email;
	private String phone;
	private String password;
}
