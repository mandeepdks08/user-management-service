package com.convo.datamodel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserRelationRequest extends DbBaseModel {
	private String fromUserId;
	private String toUserId;
	private RelationRequestType relationRequestType;
	private State state;
}
