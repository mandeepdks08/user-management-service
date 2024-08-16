package com.convo.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "userrelation")
@Getter
@Setter
@SuperBuilder
public class UserRelation extends DbBaseModel {
	@Column(name = "userid")
	private String userId;
	@Column(name = "relateduserid")
	private String relatedUserId;
	@Column(name = "relationtype")
	private RelationType relationType;
}
