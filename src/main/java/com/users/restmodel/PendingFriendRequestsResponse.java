package com.users.restmodel;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PendingFriendRequestsResponse {
	private List<PendingFriendRequest> pendingFriendRequests;
}
