package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QHometeachingUser.hometeachingUser;
import static com.personalapp.hometeaching.model.QPerson.person;
import static com.personalapp.hometeaching.model.QUserOrganization.userOrganization;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserOrganizationIds;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.repository.HometeachingUserRepository;

@Repository
public class HometeachingUserRepositoryImpl extends
		RepositoryImpl<HometeachingUser, Long> implements
		HometeachingUserRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public HometeachingUser findDetailedByUsername(String username) {
		logger.info("entering the find detailed user by username method");
		JPAQuery query = getDetailedForLogin();
		query.where(hometeachingUser.username.eq(username));
		return query.singleResult(hometeachingUser);
	}

	@Override
	public HometeachingUser findDetailedById(Long id) {
		logger.info("entering the find detailed user by id method");
		JPAQuery query = getDetailed();
		query.where(hometeachingUser.id.eq(id));
		return query.singleResult(hometeachingUser);
	}

	@Override
	public List<HometeachingUser> getAllUsers() {
		logger.info("entering the get all users method");
		JPAQuery query = getDetailed();
		return query.list(hometeachingUser);
	}

	private JPAQuery getDetailed() {
		JPAQuery query = getDetailedForLogin();
		query.where(userOrganization.organizationId
				.in(getCurrentUserOrganizationIds()));
		return query;
	}

	private JPAQuery getDetailedForLogin() {
		JPAQuery query = jpaFrom(hometeachingUser);
		query.leftJoin(hometeachingUser.userRoles).fetch();
		query.leftJoin(hometeachingUser.person, person).fetch();
		query.leftJoin(person.family).fetch();
		query.leftJoin(hometeachingUser.userOrganizations, userOrganization)
				.fetch();
		query.distinct();
		return query;
	}
}
