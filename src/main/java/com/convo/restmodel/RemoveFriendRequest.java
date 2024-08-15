package com.convo.restmodel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RemoveFriendRequest {
	private String friendUserId;
}
