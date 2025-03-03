package com.users.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "userrelationrequest")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationRequest extends DbBaseModel {
	@Column(name = "fromuserid")
	private String fromUserId;
	@Column(name = "touserid")
	private String toUserId;
	@Column(name = "relationrequesttype")
	@Enumerated(EnumType.STRING)
	private RelationRequestType relationRequestType;
	@Column(name = "state")
	@Enumerated(EnumType.STRING)
	private State state;

	public UserRelationRequest basicInfo() {
		return UserRelationRequest.builder().id(id).fromUserId(fromUserId).createdOn(createdOn).build();
	}
}
