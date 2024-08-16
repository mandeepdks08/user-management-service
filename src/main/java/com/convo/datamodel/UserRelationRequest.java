package com.convo.datamodel;

import javax.persistence.Column;
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
	@Column(name = "fromuserid")
	private String fromUserId;
	@Column(name = "touserid")
	private String toUserId;
	@Column(name = "relationrequesttype")
	private RelationRequestType relationRequestType;
	@Column(name = "state")
	private State state;
}
