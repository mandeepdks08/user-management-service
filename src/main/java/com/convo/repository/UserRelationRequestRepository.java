package com.convo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.convo.datamodel.UserRelationRequest;

@Repository
public interface UserRelationRequestRepository extends JpaRepository<UserRelationRequest, Long> {
	
}
