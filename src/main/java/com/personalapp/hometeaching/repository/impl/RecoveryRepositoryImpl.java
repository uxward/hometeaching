package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QHometeachingUser.hometeachingUser;
import static com.personalapp.hometeaching.model.QResetPassword.resetPassword;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Recovery;
import com.personalapp.hometeaching.repository.RecoveryRepository;

@Repository
public class RecoveryRepositoryImpl extends RepositoryImpl<Recovery, Long> implements RecoveryRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public Recovery findByToken(String token) {
		logger.info("Entering the find recover by token method");
		JPAQuery query = jpaFrom(resetPassword);
		query.leftJoin(resetPassword.user, hometeachingUser).fetch();
		query.leftJoin(hometeachingUser.userOrganizations).fetch();
		query.leftJoin(hometeachingUser.userRoles).fetch();
		query.where(resetPassword.token.eq(token));
		return query.singleResult(resetPassword);
	}
}
