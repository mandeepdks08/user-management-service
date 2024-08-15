package com.convo.datamodel;

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
	private String userId;
	private String relatedUserId;
	private RelationType relationType;
}
