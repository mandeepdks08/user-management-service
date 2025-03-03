package com.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.users.datamodel.RelationType;
import com.users.datamodel.UserRelation;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
	public UserRelation findByUserIdAndRelatedUserId(String userId, String relatedUserId);

	public UserRelation findByUserIdAndRelatedUserIdAndRelationType(String userId, String relatedUserId,
			RelationType relationType);

	public List<UserRelation> findByUserIdAndRelationType(String userId, RelationType relationType);
}
