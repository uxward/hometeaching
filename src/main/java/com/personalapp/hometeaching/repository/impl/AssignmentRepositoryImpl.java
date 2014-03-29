package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QAssignment.assignment;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.repository.AssignmentRepository;

@Repository
public class AssignmentRepositoryImpl extends RepositoryImpl<Assignment, Long>
		implements AssignmentRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public Assignment findByCompanionIdAndFamilyId(Long companionId,
			Long familyId) {
		logger.info("Entering the find by companion id and family id method");
		JPAQuery query = jpaFrom(assignment);
		query.where(assignment.active.isTrue())
				.where(assignment.companionId.eq(companionId))
				.where(assignment.familyId.eq(familyId));
		return query.singleResult(assignment);
	}
}
