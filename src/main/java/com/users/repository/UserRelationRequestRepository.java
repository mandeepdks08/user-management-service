package com.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.users.datamodel.RelationRequestType;
import com.users.datamodel.State;
import com.users.datamodel.UserRelationRequest;

@Repository
public interface UserRelationRequestRepository extends JpaRepository<UserRelationRequest, Long> {
	public List<UserRelationRequest> findByToUserIdAndRelationRequestTypeAndState(String toUserId,
			RelationRequestType type, State state);
}
