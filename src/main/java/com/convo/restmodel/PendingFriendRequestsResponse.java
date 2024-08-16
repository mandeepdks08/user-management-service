package com.convo.restmodel;

import java.util.List;

import com.convo.datamodel.UserRelationRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PendingFriendRequestsResponse {
	private List<UserRelationRequest> pendingFriendRequests;
}
