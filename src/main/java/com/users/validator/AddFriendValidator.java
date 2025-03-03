package com.users.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.users.datamodel.User;
import com.users.handler.UserRelationHandler;
import com.users.restmodel.AddFriendRequest;
import com.users.util.SystemContextHolder;

@Component
public class AddFriendValidator implements Validator {

	@Autowired
	private UserRelationHandler userRelationHandler;

	@Override
	public boolean supports(Class<?> clazz) {
		return AddFriendRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (!(target instanceof AddFriendRequest)) {
			return;
		}
		AddFriendRequest addFriendRequest = (AddFriendRequest) target;
		User loggedInUser = SystemContextHolder.getLoggedInUser();

		if (StringUtils.isBlank(addFriendRequest.getFriendUserId())) {
			errors.rejectValue("friendUserId", "friendUserId.invalid", "friendUserId cannot be empty");
		} else if (addFriendRequest.getFriendUserId().equals(loggedInUser.getUserId())) {
			errors.rejectValue("friendUserId", "friendUserId.invalid",
					"friendUserId cannot be same as the logged in user id");
		}

		String friendUserId = addFriendRequest.getFriendUserId();
		if (userRelationHandler.isBlockedByUser(friendUserId, loggedInUser.getUserId())) {
			errors.rejectValue("friendUserId", "friendUserId.invalid", "Cannot send friend request to the user id "
					+ friendUserId + " because you are blocked by that user");
		} else if (userRelationHandler.isBlockedByUser(loggedInUser.getUserId(), friendUserId)) {
			errors.reject("friendUserId.invalid", "Cannot send friend request to the user id " + friendUserId
					+ " because you have blocked that user. Please unblock the user first to send request.");
		}

		boolean requestPendingFromFriend = userRelationHandler.getPendingFriendRequests().stream()
				.anyMatch(pendingRequest -> pendingRequest.getFromUserId().equals(friendUserId));
		if (requestPendingFromFriend) {
			errors.reject("friendUserId.invalid",
					"You already have a friend request pending from user id " + friendUserId);
		}
	}

}
