package com.personalapp.hometeaching.repository.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.FamilyStatus.getIdsFromList;
import static com.personalapp.hometeaching.model.Organization.ELDERS;
import static com.personalapp.hometeaching.model.Organization.HIGH_PRIEST;
import static com.personalapp.hometeaching.model.Organization.RELIEF_SOCIETY;
import static com.personalapp.hometeaching.model.QAssignment.assignment;
import static com.personalapp.hometeaching.model.QCompanion.companion;
import static com.personalapp.hometeaching.model.QFamily.family;
import static com.personalapp.hometeaching.model.QFamilyOrganization.familyOrganization;
import static com.personalapp.hometeaching.model.QPerson.person;
import static com.personalapp.hometeaching.model.QPersonCompanion.personCompanion;
import static com.personalapp.hometeaching.model.QVisit.visit;
import static com.personalapp.hometeaching.security.SecurityUtils.getAllOrganizationIds;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserOrganizationIds;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyStatus;
import com.personalapp.hometeaching.model.QPerson;
import com.personalapp.hometeaching.repository.FamilyRepository;

@Repository
public class FamilyRepositoryImpl extends RepositoryImpl<Family, Long> implements FamilyRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public List<Family> getAllNotMovedFamilies() {
		logger.info("entering the get all not moved families method");
		JPAQuery query = getNotMovedFamilyQuery(getAllOrganizationIds());
		query.orderBy(family.familyName.asc());
		return query.list(family);
	}

	@Override
	public List<Family> getAllNotMovedFamiliesByStatus(List<FamilyStatus> statuses) {
		logger.info("entering the get all not moved families method");
		JPAQuery query = getNotMovedFamilyQuery(getAllOrganizationIds());
		query.where(family.familyStatusId.in(getIdsFromList(statuses)));
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
		JPAQuery query = getFamilyQuery(getAllOrganizationIds());
		query.where(family.id.eq(id)).distinct();
		return query.singleResult(family);
	}

	@Override
	public List<Family> getAllFamiliesWithoutCompanion(boolean visitingTeaching) {
		logger.info("entering the get all families without home teaching companion method");
		JPAQuery query = getNotMovedFamilyQuery(getCurrentUserOrganizationIds());
		// TODO make this not bad and ugly
		List<Long> assignedFamilyIds = getAllFamilyIdsWithCompanion(visitingTeaching);
		if (!assignedFamilyIds.isEmpty()) {
			query.where(family.assignments.isEmpty().or(family.id.notIn(assignedFamilyIds)));
		}
		query.distinct();
		query.orderBy(family.familyName.asc());
		return query.list(family);
	}

	@Override
	public List<Tuple> getFamilyStatusPercentage(List<Long> organizationIds) {
		logger.info("Entering the get all family statuse percentages method");
		JPAQuery query = jpaFrom(family);
		query.where(getFamiliesMatchingOrganizationsSubQuery(organizationIds).exists());
		query.where(family.familyMoved.isNull().or(family.familyMoved.isFalse()));
		return query.groupBy(family.familyStatusId).list(family.id.count(), family.familyStatusId);
	}

	@Override
	public List<Family> getAllFamiliesAndVisits() {
		JPAQuery query = getFamilyQuery(getAllOrganizationIds());
		query.where(family.familyMoved.isFalse());
		query.leftJoin(family.visits).fetch();
		return query.list(family);
	}

	@Override
	public List<Tuple> getVisitPercentageDetails(Integer month, Integer year, Long organizationId) {
		logger.info("Entering the get visit percentage details method for month {} and year {}", month, year);
		JPAQuery query = jpaFrom(family);
		query.leftJoin(family.visits, visit);
		query.where(visit.isNotNull(), visit.month.eq(month), visit.year.eq(year), visit.organizationId.eq(organizationId));
		query.where(getFamiliesMatchingOrganizationsSubQuery(getCurrentUserOrganizationIds()).exists());
		return query.groupBy(family.familyStatusId).list(family.id.count(), family.familyStatusId, visit.visited.castToNum(Integer.class).avg());
	}

	private List<Long> getAllFamilyIdsWithCompanion(boolean visitingTeaching) {
		logger.info("entering the get all families with a companion method");
		JPAQuery query = jpaFrom(family);
		query.leftJoin(family.assignments, assignment);
		query.leftJoin(assignment.companion, companion);
		if (visitingTeaching) {
			query.where(assignment.isNotNull(), assignment.active.isTrue(), companion.active.isTrue(), companion.organizationId.in(newArrayList(RELIEF_SOCIETY.getId())));
		} else {
			query.where(assignment.isNotNull(), assignment.active.isTrue(), companion.active.isTrue(), companion.organizationId.in(newArrayList(ELDERS.getId(), HIGH_PRIEST.getId())));
		}
		query.orderBy(family.familyName.asc());
		return query.list(family.id);
	}

	private JPAQuery getNotMovedFamilyQuery(List<Long> organizationIds) {
		JPAQuery query = getFamilyQuery(organizationIds);
		query.where(family.familyMoved.isNull().or(family.familyMoved.isFalse()));
		return query;
	}

	private JPAQuery getMovedFamilyQuery() {
		JPAQuery query = getFamilyQuery(getAllOrganizationIds());
		query.where(family.familyMoved.isNotNull().and(family.familyMoved.isTrue()).and(family.recordsMoved.isFalse()));
		return query;
	}

	private JPAQuery getFamilyQuery(List<Long> organizationIds) {
		JPAQuery query = jpaFrom(family);
		QPerson compPerson = QPerson.person;
		query.leftJoin(family.people, person).fetch();
		query.leftJoin(family.familyOrganizations, familyOrganization).fetch();
		query.leftJoin(family.assignments, assignment).fetch();
		query.leftJoin(assignment.companion, companion).fetch();
		query.leftJoin(companion.companions, personCompanion).fetch();
		query.leftJoin(personCompanion.person, compPerson).fetch();
		query.leftJoin(compPerson.family).fetch();
		query.where(familyOrganization.organizationId.in(organizationIds));
		query.distinct();
		return query;
	}

	private JPASubQuery getFamiliesMatchingOrganizationsSubQuery(List<Long> organizationIds) {
		JPASubQuery q = new JPASubQuery().from(familyOrganization);
		q.where(familyOrganization.family.eq(family), familyOrganization.organizationId.in(organizationIds));
		return q;
	}
}
