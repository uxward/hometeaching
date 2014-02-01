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
public class VisitRepositoryImpl extends RepositoryImpl<Visit, Long> implements
		VisitRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public List<Tuple> getAllVisits() {
		logger.info("Entering the get all visits method");
		JPAQuery query = jpaFrom(visit);
		return query.groupBy(visit.month, visit.year).list(
				visit.intVisited.avg(), visit.month, visit.year);
	}

	@Override
	public List<Visit> getByFamilyId(Long familyId) {
		logger.info(
				"Entering the get all visits by family id method with family id: {}",
				familyId);
		JPAQuery query = jpaFrom(visit);
		query.where(visit.familyId.eq(familyId)).orderBy(visit.year.asc(),
				visit.month.asc());
		return query.list(visit);
	}
}
