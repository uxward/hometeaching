package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.Organization.WARD;
import static com.personalapp.hometeaching.model.Organization.fromId;
import static com.personalapp.hometeaching.security.SecurityUtils.getAllOrganizationIds;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.Tuple;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.repository.FamilyRepository;
import com.personalapp.hometeaching.repository.VisitRepository;
import com.personalapp.hometeaching.service.DashboardService;
import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.OrganizationViewModel;
import com.personalapp.hometeaching.view.VisitPercentageViewModel;
import com.personalapp.hometeaching.view.WardFamilyStatusViewModel;
import com.personalapp.hometeaching.view.WardVisitPercentageViewModel;

@Service
public class DashboardServiceImpl implements DashboardService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private FamilyRepository familyRepo;

	@Autowired
	private VisitRepository visitRepo;

	@Override
	public List<WardVisitPercentageViewModel> getVisitPercentage(List<Long> organizationIds) {
		logger.info("getting the visit percentage statistics");
		List<WardVisitPercentageViewModel> wardVisits = newArrayList();

		for (Long organizationId : organizationIds) {
			List<VisitPercentageViewModel> orgVisits = newArrayList();

			for (Tuple tupleVisit : visitRepo.getAllVisitsByOrganization(organizationId)) {
				VisitPercentageViewModel visit = new VisitPercentageViewModel(tupleVisit);
				orgVisits.add(visit);
			}
			wardVisits.add(new WardVisitPercentageViewModel(orgVisits, new OrganizationViewModel(Organization.fromId(organizationId), null)));
		}

		return wardVisits;
	}

	@Override
	public List<WardFamilyStatusViewModel> getFamilyStatusPercentage(List<Long> organizationIds) {
		List<WardFamilyStatusViewModel> familyStatuses = newArrayList();

		// add family status for each organization
		for (Long organizationId : organizationIds) {
			if (WARD.equals(Organization.fromId(organizationId))) {
				List<Tuple> tupleStatuses = familyRepo.getFamilyStatusPercentage(getAllOrganizationIds());
				OrganizationViewModel organization = new OrganizationViewModel(WARD, getTotalFamiliesFromTuple(tupleStatuses));
				familyStatuses.add(new WardFamilyStatusViewModel(getFamilyStatusFromTuple(tupleStatuses), newArrayList(organization)));
			} else {
				List<Tuple> tupleStatuses = familyRepo.getFamilyStatusPercentage(newArrayList(organizationId));
				OrganizationViewModel organization = new OrganizationViewModel(fromId(organizationId), getTotalFamiliesFromTuple(tupleStatuses));
				familyStatuses.add(new WardFamilyStatusViewModel(getFamilyStatusFromTuple(tupleStatuses), newArrayList(organization)));
			}
		}
		return familyStatuses;
	}

	@Override
	public List<FamilyStatusViewModel> getVisitPercentageDetails(Integer month, Integer year, Long organizationId) {
		List<Tuple> tupleStatuses = familyRepo.getVisitPercentageDetails(month, year, organizationId);
		return getFamilyStatusFromTuple(tupleStatuses);
	}

	private List<FamilyStatusViewModel> getFamilyStatusFromTuple(List<Tuple> tupleStatuses) {
		List<FamilyStatusViewModel> statuses = newArrayList();

		for (Tuple tupleStatus : tupleStatuses) {
			FamilyStatusViewModel status = new FamilyStatusViewModel(tupleStatus, getTotalFamiliesFromTuple(tupleStatuses));
			statuses.add(status);
		}

		return statuses;
	}

	private Long getTotalFamiliesFromTuple(List<Tuple> tupleStatuses) {
		Long totalFamilies = 0L;
		for (Tuple tupleStatus : tupleStatuses) {
			totalFamilies += (Long) tupleStatus.toArray()[0];
		}
		return totalFamilies;
	}
}
