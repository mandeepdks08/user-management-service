package com.convo.restmodel;

import java.util.List;

import com.convo.datamodel.UserBasicInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FriendsListResponse {
	private List<UserBasicInfo> friendsList;
}
