package com.convo.datamodel;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRelation {
	private Long id;
	private String userId;
	private String relatedUserId;
	private RelationType relationType;
	private LocalDateTime createdOn;
	private LocalDateTime processedOn;
}
