package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QFamily.family;
import static com.personalapp.hometeaching.model.QFeedback.feedback;
import static com.personalapp.hometeaching.model.QHometeachingUser.hometeachingUser;
import static com.personalapp.hometeaching.model.QPerson.person;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Feedback;
import com.personalapp.hometeaching.repository.FeedbackRepository;

@Repository
public class FeedbackRepositoryImpl extends RepositoryImpl<Feedback, Long> implements FeedbackRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public List<Feedback> getAllFeedback() {
		logger.info("Entering the get all feedback method");
		JPAQuery query = jpaFrom(feedback);
		query.leftJoin(feedback.user, hometeachingUser).fetch();
		query.leftJoin(hometeachingUser.person, person).fetch();
		query.leftJoin(person.family, family).fetch();
		query.leftJoin(family.familyOrganizations).fetch();
		query.distinct();
		return query.list(feedback);
	}
}
