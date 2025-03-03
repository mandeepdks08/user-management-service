package com.users.restmodel;

import java.time.LocalDateTime;

import com.users.datamodel.UserBasicInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingFriendRequest {
	private Long id;
	private LocalDateTime requestDateTime;
	private UserBasicInfo userDetails;
}
