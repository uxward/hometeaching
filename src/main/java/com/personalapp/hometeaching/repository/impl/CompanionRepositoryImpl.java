package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QAssignment.assignment;
import static com.personalapp.hometeaching.model.QCompanion.companion;
import static com.personalapp.hometeaching.model.QFamily.family;
import static com.personalapp.hometeaching.model.QFamilyOrganization.familyOrganization;
import static com.personalapp.hometeaching.model.QPerson.person;
import static com.personalapp.hometeaching.model.QPersonCompanion.personCompanion;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.repository.CompanionRepository;

@Repository
public class CompanionRepositoryImpl extends RepositoryImpl<Companion, Long> implements CompanionRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public Companion findDetailedById(Long id) {
		logger.info("entering the find detailed companion by id method");
		JPAQuery query = getCompanionDetailQuery();
		query.leftJoin(family.people).fetch();
		query.where(companion.id.eq(id)).distinct();
		return query.singleResult(companion);
	}

	@Override
	public List<Companion> findAllDetailedCompanionsByPerson(Long personId) {
		JPAQuery query = getCompanionDetailQuery();
		query.leftJoin(family.people).fetch();
		query.where(companion.id.in(getCompanionsWithPersonSubQuery(personId)), companion.active.isTrue()).distinct();
		return query.list(companion);
	}

	@Override
	public List<Companion> getAllCompanionsAndActiveFamilies(Organization organization) {
		JPAQuery query = getCompanionDetailQuery();
		query.where(companion.active.eq(true), companion.organizationId.eq(organization.getId()));
		return query.list(companion);
	}

	@Override
	public Companion getCompanionAndActiveFamilies(Long companionId) {
		JPAQuery query = getCompanionDetailQuery();
		query.where(companion.active.isTrue());
		query.where(companion.id.eq(companionId));
		return query.singleResult(companion);
	}

	private JPAQuery getCompanionDetailQuery() {
		JPAQuery query = jpaFrom(companion);
		query.leftJoin(companion.companions, personCompanion).fetch();
		query.leftJoin(personCompanion.person, person).fetch();
		query.leftJoin(person.family).fetch();
		query.leftJoin(companion.assignments, assignment).fetch();
		query.leftJoin(assignment.family, family).fetch();
		query.leftJoin(family.people).fetch();
		query.leftJoin(family.familyOrganizations, familyOrganization).fetch();
		query.distinct();
		return query;
	}

	private List<Long> getCompanionsWithPersonSubQuery(Long personId) {
		JPAQuery q = jpaFrom(companion);
		q.leftJoin(companion.companions, personCompanion);
		q.where(personCompanion.personId.eq(personId));
		return q.list(companion.id);
	}
}
