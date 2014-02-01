package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QAssignment.assignment;
import static com.personalapp.hometeaching.model.QCompanion.companion;
import static com.personalapp.hometeaching.model.QFamily.family;
import static com.personalapp.hometeaching.model.QPerson.person;
import static com.personalapp.hometeaching.model.QPersonCompanion.personCompanion;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.PersonCompanion;
import com.personalapp.hometeaching.repository.PersonCompanionRepository;

@Repository
public class PersonCompanionRepositoryImpl extends
		RepositoryImpl<PersonCompanion, Long> implements
		PersonCompanionRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public List<PersonCompanion> getDetailedByPersonId(Long id) {
		logger.info("entering the get all companions method");
		JPAQuery query = jpaFrom(personCompanion);
		query.leftJoin(personCompanion.person, person);
		query.leftJoin(personCompanion.companion, companion).fetch();
		query.leftJoin(companion.assignments, assignment).fetch();
		query.leftJoin(assignment.family, family).fetch();
		query.leftJoin(family.people).fetch();
		query.where(person.id.eq(id)).distinct();
		return query.list(personCompanion);
	}

}
