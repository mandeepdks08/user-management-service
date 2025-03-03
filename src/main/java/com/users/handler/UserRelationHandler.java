package com.users.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.users.datamodel.ActionType;
import com.users.datamodel.RelationRequestType;
import com.users.datamodel.RelationType;
import com.users.datamodel.State;
import com.users.datamodel.User;
import com.users.datamodel.UserBasicInfo;
import com.users.datamodel.UserRelation;
import com.users.datamodel.UserRelationRequest;
import com.users.repository.UserRelationRepository;
import com.users.repository.UserRelationRequestRepository;
import com.users.repository.UserRepository;
import com.users.restmodel.PendingFriendRequest;
import com.users.util.SystemContextHolder;

@Component
public class UserRelationHandler {

	@Autowired
	private UserRelationRepository userRelationRepo;

	@Autowired
	private UserRelationRequestRepository relationRequestRepo;

	@Autowired
	private UserRepository userRepo;

	public void saveUserRelationRequest(UserRelationRequest relationRequest) {
		User user = SystemContextHolder.getLoggedInUser();
		relationRequestRepo.save(UserRelationRequest.builder().fromUserId(user.getUserId())
				.toUserId(relationRequest.getToUserId()).relationRequestType(relationRequest.getRelationRequestType())
				.state(State.NEW).createdOn(LocalDateTime.now()).processedOn(LocalDateTime.now()).build());
	}

	public void processUserRelationRequest(Long id, ActionType actionType) throws Exception {
		User user = SystemContextHolder.getLoggedInUser();
		UserRelationRequest relationRequest = relationRequestRepo.findById(id).orElse(null);
		if (relationRequest == null || !StringUtils.equals(user.getUserId(), relationRequest.getToUserId())) {
			throw new Exception("Unauthorized");
		}
		if (relationRequest.getState().equals(State.NEW)) {
			RelationRequestType type = relationRequest.getRelationRequestType();
			if (type.equals(RelationRequestType.FRIEND_REQUEST)) {
				processFriendRequest(relationRequest, actionType);
			}
			relationRequest.setState(State.PROCESSED);
			relationRequest.setProcessedOn(LocalDateTime.now());
			relationRequestRepo.save(relationRequest);
		} else {
			throw new Exception("Friend request accepted already!");
		}
	}

	public void removeFriend(String friendUserId) {
		User loggedInUser = SystemContextHolder.getLoggedInUser();
		UserRelation dbUserRelation = userRelationRepo.findByUserIdAndRelatedUserIdAndRelationType(
				loggedInUser.getUserId(), friendUserId, RelationType.FRIEND);
		if (dbUserRelation != null) {
			UserRelation friendUserRelation = userRelationRepo.findByUserIdAndRelatedUserIdAndRelationType(friendUserId,
					loggedInUser.getUserId(), RelationType.FRIEND);
			friendUserRelation.setRelationType(RelationType.UNFRIEND);
			friendUserRelation.setProcessedOn(LocalDateTime.now());

			dbUserRelation.setRelationType(RelationType.UNFRIEND);
			dbUserRelation.setProcessedOn(LocalDateTime.now());

			userRelationRepo.saveAll(Arrays.asList(dbUserRelation, friendUserRelation));
		}
	}

	public List<UserBasicInfo> getFriendsList() {
		User user = SystemContextHolder.getLoggedInUser();
		List<UserRelation> userRelationList = userRelationRepo.findByUserIdAndRelationType(user.getUserId(),
				RelationType.FRIEND);
		List<User> friendUsersList = userRepo.findByUserIdIn(
				userRelationList.stream().map(UserRelation::getRelatedUserId).collect(Collectors.toList()));
		return friendUsersList.stream().map(User::basicInfo).collect(Collectors.toList());
	}

	public void blockUser(String blockUserId) {
		User user = SystemContextHolder.getLoggedInUser();
		removeFriend(blockUserId);
		UserRelation dbUserRelation = userRelationRepo.findByUserIdAndRelatedUserId(user.getUserId(), blockUserId);
		if (dbUserRelation == null) {
			dbUserRelation = UserRelation.builder().userId(user.getUserId()).relatedUserId(blockUserId)
					.relationType(RelationType.BLOCKED).createdOn(LocalDateTime.now()).processedOn(LocalDateTime.now())
					.build();
		} else {
			dbUserRelation.setRelationType(RelationType.BLOCKED);
			dbUserRelation.setProcessedOn(LocalDateTime.now());
		}
		userRelationRepo.save(dbUserRelation);
	}

	public void unblockUser(String unblockUserId) {
		User user = SystemContextHolder.getLoggedInUser();
		UserRelation dbUserRelation = userRelationRepo.findByUserIdAndRelatedUserId(user.getUserId(), unblockUserId);
		if (dbUserRelation == null) {
			dbUserRelation = UserRelation.builder().userId(user.getUserId()).relatedUserId(unblockUserId)
					.relationType(RelationType.UNBLOCKED).createdOn(LocalDateTime.now())
					.processedOn(LocalDateTime.now()).build();
		} else {
			dbUserRelation.setRelationType(RelationType.UNBLOCKED);
			dbUserRelation.setProcessedOn(LocalDateTime.now());
		}
		userRelationRepo.save(dbUserRelation);
	}

	public List<UserRelationRequest> getPendingFriendRequests() {
		User user = SystemContextHolder.getLoggedInUser();
		List<UserRelationRequest> pendingFriendRequests = ObjectUtils
				.firstNonNull(relationRequestRepo.findByToUserIdAndRelationRequestTypeAndState(user.getUserId(),
						RelationRequestType.FRIEND_REQUEST, State.NEW), new ArrayList<>());
		pendingFriendRequests = pendingFriendRequests.stream().map(UserRelationRequest::basicInfo)
				.collect(Collectors.toList());
		return ObjectUtils.firstNonNull(pendingFriendRequests, new ArrayList<>());
	}

	public List<PendingFriendRequest> getPendingFriendRequestsResponse() {
		List<UserRelationRequest> pendingFriendRequests = getPendingFriendRequests();
		List<User> friendRequestUsers = userRepo.findByUserIdIn(
				pendingFriendRequests.stream().map(UserRelationRequest::getFromUserId).collect(Collectors.toList()));
		Map<String, UserRelationRequest> pendingFriendRequestUserIdMap = pendingFriendRequests.stream()
				.collect(Collectors.toMap(relationRequest -> relationRequest.getFromUserId(), Function.identity(),
						(e1, e2) -> e2));
		return friendRequestUsers.stream().map(user -> {
			LocalDateTime friendRequestDateTime = pendingFriendRequestUserIdMap.get(user.getUserId()).getCreatedOn();
			Long id = pendingFriendRequestUserIdMap.get(user.getUserId()).getId();
			return PendingFriendRequest.builder().id(id).requestDateTime(friendRequestDateTime)
					.userDetails(user.basicInfo()).build();
		}).collect(Collectors.toList());
	}

	public boolean isBlockedByUser(String userId1, String userId2) {
		UserRelation userRelation = userRelationRepo.findByUserIdAndRelatedUserId(userId1, userId2);
		return userRelation != null && userRelation.getRelationType().equals(RelationType.BLOCKED);
	}

	private void processFriendRequest(UserRelationRequest relationRequest, ActionType actionType) {
		User user = SystemContextHolder.getLoggedInUser();
		String fromUserId = relationRequest.getFromUserId();
		String toUserId = relationRequest.getToUserId();
		if (relationRequest.getToUserId().equals(user.getUserId())) {
			UserRelation friendUserRelation = userRelationRepo.findByUserIdAndRelatedUserId(fromUserId, toUserId);
			UserRelation loggedInUserRelation = userRelationRepo.findByUserIdAndRelatedUserId(toUserId, fromUserId);
			if (ActionType.ACCEPT.equals(actionType)) {
				if (friendUserRelation == null) {
					friendUserRelation = UserRelation.builder().userId(fromUserId).relatedUserId(toUserId)
							.relationType(RelationType.FRIEND).createdOn(LocalDateTime.now())
							.processedOn(LocalDateTime.now()).build();
				} else {
					friendUserRelation.setRelationType(RelationType.FRIEND);
					friendUserRelation.setProcessedOn(LocalDateTime.now());
				}
				if (loggedInUserRelation == null) {
					loggedInUserRelation = UserRelation.builder().userId(toUserId).relatedUserId(fromUserId)
							.relationType(RelationType.FRIEND).createdOn(LocalDateTime.now())
							.processedOn(LocalDateTime.now()).build();
				} else {
					loggedInUserRelation.setRelationType(RelationType.FRIEND);
					loggedInUserRelation.setProcessedOn(LocalDateTime.now());
				}
				userRelationRepo.saveAll(Arrays.asList(friendUserRelation, loggedInUserRelation));
			}
		}
	}

}
