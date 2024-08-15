package com.convo.datamodel;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DbBaseModel {
	protected Long id;
	protected LocalDateTime createdOn;
	protected LocalDateTime processedOn;
}
