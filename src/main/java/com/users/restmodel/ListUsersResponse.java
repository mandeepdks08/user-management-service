package com.users.restmodel;

import java.util.List;

import com.users.datamodel.User;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ListUsersResponse extends BaseResponse {
	private List<User> users;
}
