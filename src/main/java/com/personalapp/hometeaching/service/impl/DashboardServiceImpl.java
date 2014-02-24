package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.Tuple;
import com.personalapp.hometeaching.repository.FamilyRepository;
import com.personalapp.hometeaching.repository.VisitRepository;
import com.personalapp.hometeaching.service.DashboardService;
import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.VisitPercentageViewModel;

@Service
public class DashboardServiceImpl implements DashboardService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private FamilyRepository familyRepo;

	@Autowired
	private VisitRepository visitRepo;

	@Override
	public List<VisitPercentageViewModel> getVisitPercentage() {
		logger.info("getting the visit percentage statistics");
		List<VisitPercentageViewModel> visits = newArrayList();

		List<Tuple> tupleVisits = visitRepo.getAllVisits();

		for (Tuple tupleVisit : tupleVisits) {
			VisitPercentageViewModel visit = new VisitPercentageViewModel(tupleVisit);
			visits.add(visit);
		}
		return visits;
	}

	@Override
	public List<FamilyStatusViewModel> getFamilyStatusPercentage(List<Long> organizationIds) {

		List<Tuple> tupleStatuses = familyRepo.getFamilyStatusPercentage(organizationIds);

		return getFamilyStatusFromTuple(tupleStatuses);
	}

	private List<FamilyStatusViewModel> getFamilyStatusFromTuple(List<Tuple> tupleStatuses) {
		List<FamilyStatusViewModel> statuses = newArrayList();

		Long familyCount = 0L;
		for (Tuple tupleStatus : tupleStatuses) {
			familyCount += (Long) tupleStatus.toArray()[0];
		}

		for (Tuple tupleStatus : tupleStatuses) {
			FamilyStatusViewModel status = new FamilyStatusViewModel(tupleStatus, familyCount);
			statuses.add(status);
		}

		return statuses;
	}

	@Override
	public List<FamilyStatusViewModel> getVisitPercentageDetails(Integer month, Integer year) {
		List<Tuple> tupleStatuses = familyRepo.getVisitPercentageDetails(month, year);
		return getFamilyStatusFromTuple(tupleStatuses);
	}
}
