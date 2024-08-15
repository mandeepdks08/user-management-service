package com.convo.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.convo.datamodel.RelationType;
import com.convo.datamodel.User;
import com.convo.datamodel.UserRelation;
import com.convo.repository.UserRelationRepository;
import com.convo.restmodel.AddFriendRequest;
import com.convo.util.SystemContextHolder;

@Component
public class AddFriendValidator implements Validator {

	@Autowired
	private UserRelationRepository userRelationRepo;

	@Override
	public boolean supports(Class<?> clazz) {
		return AddFriendRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AddFriendRequest addFriendRequest = (AddFriendRequest) target;
		User loggedInUser = SystemContextHolder.getLoggedInUser();

		if (StringUtils.isBlank(addFriendRequest.getFriendUserId())) {
			errors.rejectValue("friendUserId", "friendUserId.invalid", "friendUserId cannot be empty");
		} else if (addFriendRequest.getFriendUserId().equals(loggedInUser.getUserId())) {
			errors.rejectValue("friendUserId", "friendUserId.invalid",
					"friendUserId cannot be same as the logged in user id");
		} else {
			String friendUserId = addFriendRequest.getFriendUserId();
			UserRelation userRelation = userRelationRepo.findByUserIdAndRelatedUserId(friendUserId,
					loggedInUser.getUserId());
			if (userRelation != null && userRelation.getRelationType().equals(RelationType.BLOCKED)) {
				errors.rejectValue("friendUserId", "friendUserId.invalid", "Cannot send friend request to the user id "
						+ friendUserId + " because you are blocked by that user");
			}
		}

	}

}
