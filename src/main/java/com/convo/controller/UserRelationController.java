package com.convo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convo.datamodel.ActionType;
import com.convo.datamodel.RelationRequestType;
import com.convo.datamodel.User;
import com.convo.datamodel.UserBasicInfo;
import com.convo.datamodel.UserRelationRequest;
import com.convo.handler.UserRelationHandler;
import com.convo.restmodel.AcceptFriendRequest;
import com.convo.restmodel.AddFriendRequest;
import com.convo.restmodel.BaseResponse;
import com.convo.restmodel.FriendsListResponse;
import com.convo.restmodel.PendingFriendRequestsResponse;
import com.convo.restmodel.RejectFriendRequest;
import com.convo.restmodel.RemoveFriendRequest;
import com.convo.util.SystemContextHolder;
import com.convo.validator.AddFriendValidator;

@RestController
@RequestMapping("/user/v1")
public class UserRelationController {

	@Autowired
	private UserRelationHandler userRelationHandler;

	@Autowired
	private AddFriendValidator addFriendValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(addFriendValidator);
	}

	@RequestMapping(value = "/friend/add", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> addFriend(@Valid @RequestBody AddFriendRequest addFriendRequest) {
		User loggedInUser = SystemContextHolder.getLoggedInUser();
		UserRelationRequest userRelationRequest = UserRelationRequest.builder().fromUserId(loggedInUser.getUserId())
				.toUserId(addFriendRequest.getFriendUserId()).relationRequestType(RelationRequestType.FRIEND_REQUEST)
				.build();
		userRelationHandler.saveUserRelationRequest(userRelationRequest);
		return new ResponseEntity<>(BaseResponse.builder().message("Request send successfully!").build(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/friend/accept", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> acceptFriendRequest(@RequestBody AcceptFriendRequest acceptFriendRequest) {
		return processFriendRequest(acceptFriendRequest.getRequestId(), ActionType.ACCEPT);
	}

	@RequestMapping(value = "/friend/reject", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> acceptFriendRequest(@RequestBody RejectFriendRequest rejectFriendRequest) {
		return processFriendRequest(rejectFriendRequest.getRequestId(), ActionType.REJECT);
	}

	@RequestMapping(value = "/friend/remove", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> removeFriend(@RequestBody RemoveFriendRequest removeFriendRequest) {
		userRelationHandler.removeFriend(removeFriendRequest.getFriendUserId());
		return new ResponseEntity<>(BaseResponse.builder().message("Success!").build(), HttpStatus.OK);
	}

	@RequestMapping(value = "/friend/list", method = RequestMethod.GET)
	protected ResponseEntity<FriendsListResponse> listFriends() {
		List<UserBasicInfo> friendsList = userRelationHandler.getFriendsList();
		return new ResponseEntity<>(FriendsListResponse.builder().friendsList(friendsList).build(), HttpStatus.OK);
	}

	@RequestMapping(value = "/friend/request/list", method = RequestMethod.GET)
	protected ResponseEntity<PendingFriendRequestsResponse> listFriendRequests() {
		return new ResponseEntity<>(
				PendingFriendRequestsResponse.builder()
						.pendingFriendRequests(userRelationHandler.getPendingFriendRequestsResponse()).build(),
				HttpStatus.OK);
	}

	private ResponseEntity<BaseResponse> processFriendRequest(Long requestId, ActionType actionType) {
		HttpStatus httpStatus = null;
		String responseMessage = null;
		try {
			userRelationHandler.processUserRelationRequest(requestId, actionType);
			responseMessage = "Success!";
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			responseMessage = e.getMessage();
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(BaseResponse.builder().message(responseMessage).build(), httpStatus);
	}

}
