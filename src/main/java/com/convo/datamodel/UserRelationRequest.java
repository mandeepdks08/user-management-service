package com.convo.datamodel;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "userrelationrequest")
@Getter
@Setter
@SuperBuilder
public class UserRelationRequest extends DbBaseModel {
	private String fromUserId;
	private String toUserId;
	private RelationRequestType relationRequestType;
	private State state;
}
