package com.users.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.users.datamodel.ActionType;
import com.users.datamodel.RelationRequestType;
import com.users.datamodel.User;
import com.users.datamodel.UserBasicInfo;
import com.users.datamodel.UserRelationRequest;
import com.users.handler.UserRelationHandler;
import com.users.restmodel.AcceptFriendRequest;
import com.users.restmodel.AddFriendRequest;
import com.users.restmodel.BaseResponse;
import com.users.restmodel.FriendsListResponse;
import com.users.restmodel.PendingFriendRequestsResponse;
import com.users.restmodel.RejectFriendRequest;
import com.users.restmodel.RemoveFriendRequest;
import com.users.util.SystemContextHolder;
import com.users.validator.AddFriendValidator;

@RestController
@RequestMapping("/user/v1")
public class UserRelationController {

	@Autowired
	private UserRelationHandler userRelationHandler;

	@Autowired
	private AddFriendValidator addFriendValidator;

	@InitBinder("addFriendRequest")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(addFriendValidator);
	}

	@RequestMapping(value = "/friend/add", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> addFriend(@Valid @RequestBody AddFriendRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream().map(err -> err.getDefaultMessage())
					.collect(Collectors.toList());
			return new ResponseEntity<>(BaseResponse.builder().errors(errors).build(), HttpStatus.BAD_REQUEST);
		}
		User loggedInUser = SystemContextHolder.getLoggedInUser();
		UserRelationRequest userRelationRequest = UserRelationRequest.builder().fromUserId(loggedInUser.getUserId())
				.toUserId(request.getFriendUserId()).relationRequestType(RelationRequestType.FRIEND_REQUEST).build();
		userRelationHandler.saveUserRelationRequest(userRelationRequest);
		return new ResponseEntity<>(BaseResponse.builder().message("Request sent successfully!").build(),
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
