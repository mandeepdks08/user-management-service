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
@Table(name = "userrelation")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRelation extends DbBaseModel {
	@Column(name = "userid")
	private String userId;
	@Column(name = "relateduserid")
	private String relatedUserId;
	@Column(name = "relationtype")
	@Enumerated(EnumType.STRING)
	private RelationType relationType;
}
