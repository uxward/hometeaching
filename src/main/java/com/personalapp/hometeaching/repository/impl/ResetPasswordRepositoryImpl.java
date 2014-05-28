package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QHometeachingUser.hometeachingUser;
import static com.personalapp.hometeaching.model.QResetPassword.resetPassword;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.QHometeachingUser;
import com.personalapp.hometeaching.model.ResetPassword;
import com.personalapp.hometeaching.repository.ResetPasswordRepository;

@Repository
public class ResetPasswordRepositoryImpl extends RepositoryImpl<ResetPassword, Long> implements ResetPasswordRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public ResetPassword findByToken(String token) {
		logger.info("Entering the find recover by token method");
		JPAQuery query = jpaFrom(resetPassword);
		query.leftJoin(resetPassword.user, hometeachingUser).fetch();
		query.where(resetPassword.token.eq(token));
		return query.singleResult(resetPassword);
	}
}
