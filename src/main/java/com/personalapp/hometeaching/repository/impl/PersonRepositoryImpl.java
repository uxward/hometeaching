package com.personalapp.hometeaching.repository.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.QCompanion.companion;
import static com.personalapp.hometeaching.model.QFamily.family;
import static com.personalapp.hometeaching.model.QHometeachingUser.hometeachingUser;
import static com.personalapp.hometeaching.model.QPerson.person;
import static com.personalapp.hometeaching.model.QPersonCompanion.personCompanion;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserOrganizationIds;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.repository.PersonRepository;

@Repository
public class PersonRepositoryImpl extends RepositoryImpl<Person, Long> implements PersonRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public Person findDetailedById(Long id) {
		logger.info("entering the find detailed person by id method");
		JPAQuery query = getPersonQuery();
		query.where(person.id.eq(id)).distinct();
		return query.singleResult(person);
	}

	@Override
	public List<Person> getByFamilyId(Long id) {
		logger.info("entering the get by family id method");
		JPAQuery query = getPersonQuery();
		query.where(family.id.eq(id)).distinct();
		return query.list(person);
	}

	@Override
	public List<Person> getAllNotMovedTeachers(boolean visitingTeaching) {
		logger.info("entering the get all unassigned hometeachers method");
		JPAQuery query = getPersonNotMovedQuery();
		if (visitingTeaching) {
			query.where(person.visitingTeacher.isTrue());
		} else {
			query.where(person.homeTeacher.isTrue());
		}
		query.orderBy(person.firstName.asc(), family.familyName.asc());
		return query.list(person);
	}

	@Override
	public List<Person> getNotCreatedUsers() {
		logger.info("entering the get not created users method");
		JPAQuery query = getPersonNotMovedQuery();
		query.where(person.user.isTrue());
		query.where(person.notIn(getCreatedUsers()));
		query.where(person.organizationId.in(getCurrentUserOrganizationIds()));
		query.orderBy(family.familyName.asc());
		return query.list(person);
	}

	private JPAQuery getPersonNotMovedQuery() {
		JPAQuery query = getPersonQuery();
		query.where(family.familyMoved.isNull().or(family.familyMoved.isFalse()));
		return query;
	}

	private JPAQuery getPersonQuery() {
		JPAQuery query = jpaFrom(person);
		query.leftJoin(person.family, family).fetch();
		query.leftJoin(family.familyOrganizations).fetch();
		query.leftJoin(person.personCompanion, personCompanion).fetch();
		query.leftJoin(personCompanion.companion, companion).fetch();
		query.distinct();
		return query;
	}

	private List<Person> getCreatedUsers() {
		List<Person> assignedPeople = newArrayList();
		JPAQuery query = jpaFrom(hometeachingUser);
		query.leftJoin(hometeachingUser.person, person).fetch();
		for (HometeachingUser user : query.list(hometeachingUser)) {
			assignedPeople.add(user.getPerson());
		}
		return assignedPeople;
	}

	private JPASubQuery getHomeTeachersSubQuery() {
		JPASubQuery q = new JPASubQuery().from(personCompanion);
		// q.where(personCompanion.person.eq(person),
		// personCompanion.active.isTrue(),
		// personCompanion.visitingTeaching.isFalse());
		return q;
	}

	private JPASubQuery getVisitingTeachersSubQuery() {
		JPASubQuery q = new JPASubQuery().from(personCompanion);
		// q.where(personCompanion.person.eq(person),
		// personCompanion.active.isTrue(),
		// personCompanion.visitingTeaching.isTrue());
		return q;
	}
}
