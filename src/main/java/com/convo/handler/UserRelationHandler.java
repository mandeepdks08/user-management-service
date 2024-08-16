package com.convo.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.convo.datamodel.ActionType;
import com.convo.datamodel.RelationRequestType;
import com.convo.datamodel.RelationType;
import com.convo.datamodel.State;
import com.convo.datamodel.User;
import com.convo.datamodel.UserRelation;
import com.convo.datamodel.UserRelationRequest;
import com.convo.repository.UserRelationRepository;
import com.convo.repository.UserRelationRequestRepository;
import com.convo.repository.UserRepository;
import com.convo.util.SystemContextHolder;

@Component
public class UserRelationHandler {

	@Autowired
	private UserRelationRepository userRelationRepo;

	@Autowired
	private UserRelationRequestRepository relationRequestRepo;

	@Autowired
	private UserRepository userRepo;

	public void saveUserRelationRequest(UserRelationRequest relationRequest) {
		// Check (fromUserId == toUserId) case in validator
		// Check if user is blocked in validator
		User user = SystemContextHolder.getLoggedInUser();
		relationRequestRepo.save(UserRelationRequest.builder().fromUserId(user.getUserId())
				.toUserId(relationRequest.getToUserId()).relationRequestType(relationRequest.getRelationRequestType())
				.state(State.NEW).createdOn(LocalDateTime.now()).processedOn(LocalDateTime.now()).build());
	}

	public void processUserRelationRequest(Long id, ActionType actionType) throws Exception {
		User user = SystemContextHolder.getLoggedInUser();
		UserRelationRequest relationRequest = relationRequestRepo.findById(id).orElse(null);
		if (relationRequest == null || !StringUtils.equalsAny(user.getUserId(), relationRequest.getFromUserId(),
				relationRequest.getToUserId())) {
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
		}
	}

	public void removeFriend(String friendUserId) {
		User loggedInUser = SystemContextHolder.getLoggedInUser();
		UserRelation dbUserRelation = userRelationRepo.findByUserIdAndRelatedUserId(loggedInUser.getUserId(),
				friendUserId);
		if (dbUserRelation != null) {
			UserRelation friendUserRelation = userRelationRepo.findByUserIdAndRelatedUserId(friendUserId,
					loggedInUser.getUserId());
			friendUserRelation.setRelationType(RelationType.UNFRIEND);
			friendUserRelation.setProcessedOn(LocalDateTime.now());

			dbUserRelation.setRelationType(RelationType.UNFRIEND);
			dbUserRelation.setProcessedOn(LocalDateTime.now());

			userRelationRepo.save(dbUserRelation);
			userRelationRepo.save(friendUserRelation);
		}
	}

	public List<User> getFriendsList() {
		User user = SystemContextHolder.getLoggedInUser();
		List<UserRelation> userRelationList = userRelationRepo.findByUserIdAndRelationType(user.getUserId(),
				RelationType.FRIEND);
		List<User> friendUsersList = userRepo.findByUserIdIn(
				userRelationList.stream().map(UserRelation::getRelatedUserId).collect(Collectors.toList()));
		return friendUsersList.stream().map(User::basicInfo).collect(Collectors.toList());
	}

	public void blockUser(String blockUserId) {
		User user = SystemContextHolder.getLoggedInUser();
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
		return pendingFriendRequests;
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
				userRelationRepo.save(friendUserRelation);
				userRelationRepo.save(loggedInUserRelation);
			}
		}
	}

}
