package com.personalapp.hometeaching.repository.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.QFamily.family;
import static com.personalapp.hometeaching.model.QHometeachingUser.hometeachingUser;
import static com.personalapp.hometeaching.model.QPerson.person;
import static com.personalapp.hometeaching.model.QPersonCompanion.personCompanion;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserOrganizationIds;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

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
	public List<Person> getAllNotMovedHometeachers() {
		logger.info("entering the get all hometeachers method");
		JPAQuery query = getPersonQuery();
		query.where(person.organizationId.in(getCurrentUserOrganizationIds()));
		query.where(person.hometeacher.eq(true));
		query.orderBy(person.firstName.asc(), family.familyName.asc());
		return query.list(person);
	}

	@Override
	public List<Person> getUnassignedHometeachingUsers() {
		logger.info("entering the get all unassigned hometeachers method");
		JPAQuery query = getPersonQuery();
		query.where(person.hometeacher.eq(true));
		query.where(person.notIn(getAssignedHometeachingUsers()));
		query.where(person.organizationId.in(getCurrentUserOrganizationIds()));
		query.orderBy(family.familyName.asc());
		return query.list(person);
	}

	private JPAQuery getPersonQuery() {
		JPAQuery query = jpaFrom(person);
		query.leftJoin(person.family, family).fetch();
		query.leftJoin(family.familyOrganizations).fetch();
		query.leftJoin(person.personCompanion, personCompanion).fetch();
		query.where(family.familyMoved.isNull().or(family.familyMoved.eq(false)));
		query.distinct();
		return query;
	}

	private List<Person> getAssignedHometeachingUsers() {
		List<Person> assignedPeople = newArrayList();
		JPAQuery query = jpaFrom(hometeachingUser);
		query.leftJoin(hometeachingUser.person, person).fetch();
		for (HometeachingUser user : query.list(hometeachingUser)) {
			assignedPeople.add(user.getPerson());
		}
		return assignedPeople;
	}
}
