package com.users.restmodel;

import java.util.List;

import com.users.datamodel.UserBasicInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FriendsListResponse {
	private List<UserBasicInfo> friendsList;
}
