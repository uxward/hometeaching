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

import com.google.common.collect.Lists;
import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.repository.CompanionRepository;
import com.personalapp.hometeaching.repository.FamilyRepository;
import com.personalapp.hometeaching.service.CompanionService;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.CompanionViewModel;
import com.personalapp.hometeaching.view.FamilyViewModel;

@Service
public class CompanionServiceImpl implements CompanionService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private CompanionRepository repo;

	@Autowired
	private FamilyRepository familyRepo;

	@Autowired
	private CompanionServiceImplHelper companionServiceImplHelper;

	@Override
	public CompanionViewModel addCompanion(Companion companion) {
		ActionStatus status = tryCreateNewCompanion(companion);
		return new CompanionViewModel(repo.findDetailedById(companion.getId()), true, status);
	}

	@Override
	public CompanionViewModel editAssignment(Companion companion) {
		// setup new companion with same families
		ActionStatus status = tryEditCompanion(companion);
		return new CompanionViewModel(repo.findDetailedById(companion.getId()), true, status);
	}

	@Override
	public FamilyViewModel addAssignment(Companion companion) {
		ActionStatus actionStatus = tryAddAssignment(companion);
		return new FamilyViewModel(familyRepo.findDetailedById(companion.getAutopopulatingAssignments().get(0).getFamilyId()), true, false, true, actionStatus);
	}

	@Override
	public ActionViewModel removeAssignment(Long companionId, Long familyId) {
		ActionStatus actionStatus = tryRemoveAssignment(companionId, familyId);
		ActionViewModel actionViewModel = new ActionViewModel();
		actionViewModel.setActionStatus(actionStatus);
		return actionViewModel;
	}

	@Override
	public ActionViewModel removeCompanion(Long companionId) {
		ActionViewModel actionViewModel = new ActionViewModel();
		actionViewModel.setActionStatus(tryRemoveCompanion(companionId));
		return actionViewModel;
	}

	@Override
	public List<CompanionViewModel> getViewModelAllHomeTeachingCompanionsAndActiveFamilies() {
		List<CompanionViewModel> companions = newArrayList();
		for (Companion companion : repo.getAllHomeTeachingCompanionsAndActiveFamilies()) {
			companions.add(new CompanionViewModel(companion, true));
		}
		return companions;
	}

	@Override
	public List<CompanionViewModel> getViewModelAllVisitingTeachingCompanionsAndActiveFamilies() {
		List<CompanionViewModel> companions = newArrayList();
		for (Companion companion : repo.getAllVisitingTeachingCompanionsAndActiveFamilies()) {
			companions.add(new CompanionViewModel(companion, true));
		}
		return companions;
	}

	@Override
	public List<Companion> getAllCompanionsAndActiveFamilies() {
		return repo.getAllHomeTeachingCompanionsAndActiveFamilies();
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
	public CompanionViewModel findDetailedCompanionViewModelById(Long id) {
		return new CompanionViewModel(findDetailedById(id), true);
	}

	@Override
	public CompanionViewModel getDetailedViewModelForCompanion(Companion companion) {
		return new CompanionViewModel(companion, true);
	}

	@Override
	public List<CompanionViewModel> getDetailedHomeTeachingViewModelsByPersonId(Long personId) {
		List<CompanionViewModel> companions = newArrayList();
		for (Companion companion : repo.findAllDetailedHomeTeachingByPerson(personId)) {
			companions.add(new CompanionViewModel(companion, true));
		}
		return companions;
	}

	@Override
	public List<CompanionViewModel> getDetailedVisitingTeachingViewModelsByPersonId(Long personId) {
		List<CompanionViewModel> companions = newArrayList();
		for (Companion companion : repo.findAllDetailedVisitingTeachingByPerson(personId)) {
			companions.add(new CompanionViewModel(companion, true));
		}
		return companions;
	}

	private ActionStatus tryCreateNewCompanion(Companion companion) {
		ActionStatus status = SUCCESS;
		try {
			companionServiceImplHelper.doCreateNewCompanion(companion);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to create a new companion: {}", e);
			status = ERROR;
		}
		return status;
	}

	private ActionStatus tryEditCompanion(Companion companion) {
		ActionStatus status = SUCCESS;
		try {
			companionServiceImplHelper.doEditCompanion(companion);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to edit a companion: {}", e);
			status = ERROR;
		}
		return status;
	}

	private ActionStatus tryRemoveAssignment(Long companionId, Long familyId) {
		ActionStatus actionStatus = SUCCESS;
		try {
			companionServiceImplHelper.doRemoveAssignment(companionId, familyId);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to remove the assignment: {}", e);
			actionStatus = ERROR;
		}
		return actionStatus;
	}

	private ActionStatus tryAddAssignment(Companion companion) {
		ActionStatus actionStatus = SUCCESS;
		try {
			companionServiceImplHelper.doAddAssignment(companion);
		} catch (Exception e) {
			logger.error("An unexpected exception occurred while adding the assignment: {}", e);
			actionStatus = ERROR;
		}
		return actionStatus;
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
