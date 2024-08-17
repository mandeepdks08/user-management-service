package com.convo.restmodel;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseResponse {
	private String message;
	private List<String> errors;
}
