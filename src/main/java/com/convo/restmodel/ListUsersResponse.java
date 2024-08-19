package com.convo.restmodel;

import java.util.List;

import com.convo.datamodel.User;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ListUsersResponse extends BaseResponse {
	private List<User> users;
}
