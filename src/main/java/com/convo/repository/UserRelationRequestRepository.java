package com.convo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.convo.datamodel.RelationRequestType;
import com.convo.datamodel.State;
import com.convo.datamodel.UserRelationRequest;

@Repository
public interface UserRelationRequestRepository extends JpaRepository<UserRelationRequest, Long> {
	public List<UserRelationRequest> findByToUserIdAndRelationRequestTypeAndState(String toUserId,
			RelationRequestType type, State state);
}
