package com.convo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.convo.datamodel.RelationType;
import com.convo.datamodel.UserRelation;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
	public UserRelation findByUserIdAndRelatedUserId(String userId, String relatedUserId);
	
	public List<UserRelation> findByUserIdAndRelationType(String userId, RelationType relationType);
}
