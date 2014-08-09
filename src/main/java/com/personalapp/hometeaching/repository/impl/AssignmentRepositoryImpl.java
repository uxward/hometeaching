package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QAssignment.assignment;
import static com.personalapp.hometeaching.model.QFamily.family;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.repository.AssignmentRepository;

@Repository
public class AssignmentRepositoryImpl extends RepositoryImpl<Assignment, Long> implements AssignmentRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public Assignment findByCompanionIdAndFamilyId(Long companionId, Long familyId) {
		logger.info("Entering the find by companion id and family id method");
		JPAQuery query = jpaFrom(assignment);
		query.where(assignment.active.isTrue()).where(assignment.companionId.eq(companionId)).where(assignment.familyId.eq(familyId));
		return query.singleResult(assignment);
	}

	@Override
	public List<Assignment> findActiveByCompanion(Long companionId) {
		logger.info("Entering the find by companion id method");
		JPAQuery query = jpaFrom(assignment);
		query.leftJoin(assignment.family, family).fetch();
		query.leftJoin(family.people).fetch();
		query.where(assignment.active.isTrue(), assignment.companionId.eq(companionId)).distinct();
		return query.list(assignment);
	}

	@Override
	public Assignment findDetailedById(Long id) {
		logger.info("Entering the find by detailed companion by id method");
		JPAQuery query = jpaFrom(assignment);
		query.where(assignment.active.isTrue()).where(assignment.id.eq(id));
		query.leftJoin(assignment.family, family).fetch();
		query.leftJoin(family.people).fetch();
		query.leftJoin(assignment.companion).fetch();
		return query.singleResult(assignment);
	}
}
