package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.repository.AssignmentRepository;
import com.personalapp.hometeaching.repository.CompanionRepository;
import com.personalapp.hometeaching.repository.FamilyRepository;
import com.personalapp.hometeaching.service.CompanionService;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.CompanionViewModel;

@Service
public class CompanionServiceImpl implements CompanionService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private CompanionRepository repo;

	@Autowired
	private FamilyRepository familyRepo;

	@Autowired
	private CompanionServiceImplHelper companionServiceImplHelper;

	@Autowired
	private AssignmentRepository assignmentRepo;

	@Override
	public CompanionViewModel addCompanion(Companion companion) {
		ActionStatus status = tryCreateNewCompanion(companion);
		return new CompanionViewModel(repo.findDetailedById(companion.getId()), true, status);
	}

	@Override
	public CompanionViewModel editCompanion(Companion companion) {
		// setup new companion with same families
		ActionStatus status = tryEditCompanion(companion);
		return new CompanionViewModel(repo.findDetailedById(companion.getId()), true, status);
	}

	@Override
	public ActionViewModel removeCompanion(Long companionId) {
		ActionViewModel actionViewModel = new ActionViewModel();
		actionViewModel.setActionStatus(tryRemoveCompanion(companionId));
		return actionViewModel;
	}

	@Override
	public List<CompanionViewModel> getViewModelAllCompanionsAndActiveFamiliesByOrganization(Organization organization) {
		List<CompanionViewModel> companions = newArrayList();
		for (Companion companion : repo.getAllCompanionsAndActiveFamilies(organization)) {
			companions.add(new CompanionViewModel(companion, true));
		}
		return companions;
	}

	@Override
	public List<Companion> getAllCompanionsAndActiveFamilies(Organization organization) {
		return repo.getAllCompanionsAndActiveFamilies(organization);
	}

	@Override
	public Companion getCompanionAndActiveFamilies(Long companionId) {
		return repo.getCompanionAndActiveFamilies(companionId);
	}

	@Override
	public Companion findDetailedById(Long id) {
		Companion companion = repo.findDetailedById(id);
		// only display active assignments
		Set<Assignment> activeAssignments = newHashSet();
		for (Assignment assignment : companion.getAssignments()) {
			if (assignment.getActive()) {
				activeAssignments.add(assignment);
			}
		}
		companion.setAssignments(activeAssignments);
		return companion;
	}

	@Override
	public CompanionViewModel getDetailedViewModelForCompanion(Companion companion) {
		return new CompanionViewModel(companion, true);
	}

	@Override
	public List<CompanionViewModel> getDetailedCompanionViewModelsByPersonId(Long personId, boolean visitingTeaching) {
		List<CompanionViewModel> companions = newArrayList();
		for (Companion companion : repo.findAllDetailedCompanionsByPerson(personId)) {
			if (companion.isVisitingTeaching() == visitingTeaching) {
				Set<Assignment> activeAssignments = newHashSet();
				for (Assignment assignment : companion.getAssignments()) {
					if (assignment.getActive()) {
						activeAssignments.add(assignment);
					}
				}
				companion.setAssignments(activeAssignments);
				companions.add(new CompanionViewModel(companion, true));
			}
		}
		return companions;
	}

	private ActionStatus tryCreateNewCompanion(Companion companion) {
		ActionStatus status = SUCCESS;
		try {
			companionServiceImplHelper.doCreateNewCompanion(companion);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to create a new companion: ", e);
			status = ERROR;
		}
		return status;
	}

	private ActionStatus tryEditCompanion(Companion companion) {
		ActionStatus status = SUCCESS;
		try {
			companionServiceImplHelper.doEditCompanion(companion);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to edit a companion: ", e);
			status = ERROR;
		}
		return status;
	}

	private ActionStatus tryRemoveCompanion(Long companionId) {
		ActionStatus actionStatus = SUCCESS;
		try {
			companionServiceImplHelper.doRemoveCompanion(companionId);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while removing the companion: {}", e);
			actionStatus = ERROR;
		}
		return actionStatus;
	}
}
