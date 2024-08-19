package com.convo.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convo.datamodel.User;
import com.convo.handler.UserHandler;
import com.convo.handler.UserLoginHandler;
import com.convo.handler.UserRelationHandler;
import com.convo.handler.UserUpdateHandler;
import com.convo.restmodel.BaseResponse;
import com.convo.restmodel.BlockUserRequest;
import com.convo.restmodel.ListUsersRequest;
import com.convo.restmodel.ListUsersResponse;
import com.convo.restmodel.LoginRequest;
import com.convo.restmodel.LoginResponse;
import com.convo.restmodel.UserRegisterRequest;

@RestController
@RequestMapping("/user/v1")
public class UserController {

	@Autowired
	private UserHandler userHandler;

	@Autowired
	private UserUpdateHandler updateHandler;

	@Autowired
	private UserLoginHandler loginHandler;

	@Autowired
	private UserRelationHandler userRelationHandler;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> register(@RequestBody UserRegisterRequest registrationRequest) {
		userHandler.register(registrationRequest);
		BaseResponse res = BaseResponse.builder().message("Registration successful!").build();
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> update(@RequestBody User user) {
		HttpStatus httpStatus = null;
		String responseMessage = null;
		try {
			updateHandler.updateUser(user);
			responseMessage = "Updated successfully";
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			responseMessage = e.getMessage();
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(BaseResponse.builder().message(responseMessage).build(), httpStatus);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
		HttpStatus httpStatus = null;
		String responseMessage = null;
		try {
			LoginResponse loginResponse = loginHandler.login(loginRequest);
			response.addCookie(new Cookie("token", loginResponse.getJwtToken()));
			responseMessage = "Login successful!";
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			responseMessage = e.getMessage();
			httpStatus = HttpStatus.BAD_REQUEST;
			e.printStackTrace();
		}
		return new ResponseEntity<>(BaseResponse.builder().message(responseMessage).build(), httpStatus);
	}

	@RequestMapping(value = "/block", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> block(@RequestBody BlockUserRequest blockUserRequest) {
		userRelationHandler.blockUser(blockUserRequest.getBlockUserId());
		return new ResponseEntity<>(BaseResponse.builder().message("Success").build(), HttpStatus.OK);
	}

	@RequestMapping(value = "/unblock", method = RequestMethod.POST)
	protected ResponseEntity<BaseResponse> unblock(@RequestBody BlockUserRequest blockUserRequest) {
		userRelationHandler.unblockUser(blockUserRequest.getBlockUserId());
		return new ResponseEntity<>(BaseResponse.builder().message("Success").build(), HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	protected ResponseEntity<ListUsersResponse> listUsers(@RequestBody ListUsersRequest request) {
		ListUsersResponse response = ListUsersResponse.builder().build();
		HttpStatus status = null;
		try {
			response.setUsers(userHandler.listUsers(request.getUserIds()));
			status = HttpStatus.OK;
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(response, status);
	}

}
