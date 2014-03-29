package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QVisit.visit;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.Visit;
import com.personalapp.hometeaching.repository.VisitRepository;

@Repository
public class VisitRepositoryImpl extends RepositoryImpl<Visit, Long> implements VisitRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public List<Tuple> getAllVisitsByOrganization(Long organizationId) {
		logger.info("Entering the get all visits method");
		JPAQuery query = jpaFrom(visit);
		query.where(visit.organizationId.eq(organizationId));
		query.orderBy(visit.year.asc(), visit.month.asc());
		return query.groupBy(visit.month, visit.year, visit.organizationId).list(visit.intVisited.avg(), visit.month, visit.year, visit.organizationId);
	}

	@Override
	public List<Visit> getHomeTeachingVisitsByFamilyId(Long familyId) {
		logger.info("Entering the get all home teaching visits by family id method with family id: {}", familyId);
		JPAQuery query = jpaFrom(visit);
		query.where(visit.familyId.eq(familyId), visit.visitingTeaching.isFalse()).orderBy(visit.year.asc(), visit.month.asc());
		return query.list(visit);
	}

	@Override
	public List<Visit> getVisitingTeachingVisitsByFamilyId(Long familyId) {
		logger.info("Entering the get all visits by family id method with family id: {}", familyId);
		JPAQuery query = jpaFrom(visit);
		query.where(visit.familyId.eq(familyId), visit.visitingTeaching.isTrue()).orderBy(visit.year.asc(), visit.month.asc());
		return query.list(visit);
	}
}
