package com.users.datamodel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserBasicInfo {
	private String name;
	private String userId;
	private Boolean enabled;
}
