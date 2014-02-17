package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QAssignment.assignment;
import static com.personalapp.hometeaching.model.QCompanion.companion;
import static com.personalapp.hometeaching.model.QFamily.family;
import static com.personalapp.hometeaching.model.QFamilyOrganization.familyOrganization;
import static com.personalapp.hometeaching.model.QPerson.person;
import static com.personalapp.hometeaching.model.QPersonCompanion.personCompanion;
import static com.personalapp.hometeaching.model.QVisit.visit;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserOrganizationIds;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.QPerson;
import com.personalapp.hometeaching.repository.FamilyRepository;

@Repository
public class FamilyRepositoryImpl extends RepositoryImpl<Family, Long> implements FamilyRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public List<Family> getAllNotMovedFamilies() {
		logger.info("entering the get all not moved families method");
		JPAQuery query = getNotMovedFamilyQuery();
		query.orderBy(family.familyName.asc());
		return query.list(family);
	}

	@Override
	public List<Family> getAllMovedFamilies() {
		logger.info("entering the get all moved families method");
		JPAQuery query = getMovedFamilyQuery();
		query.orderBy(family.familyName.asc());
		return query.list(family);
	}

	@Override
	public Family findDetailedById(Long id) {
		logger.info("entering the getDetailedById method");
		JPAQuery query = getFamilyQuery();
		query.where(family.id.eq(id)).distinct();
		return query.singleResult(family);
	}

	@Override
	public List<Family> getAllFamiliesWithoutCompanion() {
		logger.info("entering the get all families without companion method");
		JPAQuery query = getNotMovedFamilyQuery();
		// TODO make this not bad and ugly
		List<Long> assignedFamilyIds = getAllFamilyIdsWithCompanion();
		if (!assignedFamilyIds.isEmpty()) {
			query.where(family.assignment.isEmpty().or(family.id.notIn(getAllFamilyIdsWithCompanion())));
		}
		query.distinct();
		query.orderBy(family.familyName.asc());
		return query.list(family);
	}

	@Override
	public List<Tuple> getFamilyStatusPercentage() {
		logger.info("Entering the get all family statuse percentages method");
		JPAQuery query = jpaFrom(family);
		query.where(getFamiliesMatchingUserOrganizationsSubQuery().exists());
		query.where(family.familyMoved.isNull().or(family.familyMoved.eq(false)));
		return query.groupBy(family.familyStatusId).list(family.id.count(), family.familyStatusId);
	}

	@Override
	public List<Family> getByCompanionId(Long companionId) {
		JPAQuery query = getNotMovedFamilyQuery();
		query.where(assignment.active.eq(true), assignment.companionId.eq(companionId));
		return query.list(family);
	}

	@Override
	public List<Family> getAllFamiliesAndVisits() {
		JPAQuery query = getFamilyQuery();
		query.leftJoin(family.visits).fetch();
		return query.list(family);
	}

	@Override
	public List<Tuple> getVisitPercentageDetails(Integer month, Integer year) {
		logger.info("Entering the get visit percentage details method for month {} and year {}", month, year);
		JPAQuery query = jpaFrom(family);
		query.leftJoin(family.visits, visit);
		query.where(visit.isNotNull(), visit.month.eq(month), visit.year.eq(year));
		query.where(getFamiliesMatchingUserOrganizationsSubQuery().exists());
		return query.groupBy(family.familyStatusId).list(family.id.count(), family.familyStatusId, visit.visited.castToNum(Integer.class).avg());
	}

	private List<Long> getAllFamilyIdsWithCompanion() {
		logger.info("entering the get all families with a companion method");
		JPAQuery query = jpaFrom(family);
		query.leftJoin(family.assignment, assignment);
		query.where(assignment.isNotNull().and(assignment.active.isTrue()));
		query.orderBy(family.familyName.asc());
		return query.list(family.id);
	}

	private JPAQuery getNotMovedFamilyQuery() {
		JPAQuery query = getFamilyQuery();
		query.where(family.familyMoved.isNull().or(family.familyMoved.eq(false)));
		return query;
	}

	private JPAQuery getMovedFamilyQuery() {
		JPAQuery query = getFamilyQuery();
		query.where(family.familyMoved.isNotNull().and(family.familyMoved.eq(true)));
		return query;
	}

	private JPAQuery getFamilyQuery() {
		JPAQuery query = jpaFrom(family);
		QPerson compPerson = QPerson.person;
		query.leftJoin(family.people, person).fetch();
		query.leftJoin(family.familyOrganizations, familyOrganization).fetch();
		query.leftJoin(family.assignment, assignment).fetch();
		query.leftJoin(assignment.companion, companion).fetch();
		query.leftJoin(companion.companions, personCompanion).fetch();
		query.leftJoin(personCompanion.person, compPerson).fetch();
		query.leftJoin(compPerson.family).fetch();
		query.where(familyOrganization.organizationId.in(getCurrentUserOrganizationIds()));
		query.distinct();
		return query;
	}

	private JPASubQuery getFamiliesMatchingUserOrganizationsSubQuery() {
		JPASubQuery q = new JPASubQuery().from(familyOrganization);
		q.where(familyOrganization.family.eq(family), familyOrganization.organizationId.in(getCurrentUserOrganizationIds()));
		return q;
	}
}
