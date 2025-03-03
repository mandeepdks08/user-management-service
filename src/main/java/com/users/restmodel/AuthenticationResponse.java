package com.users.restmodel;

import com.users.datamodel.User;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class AuthenticationResponse extends BaseResponse {
	private Boolean authenticated;
	private User user;
}
