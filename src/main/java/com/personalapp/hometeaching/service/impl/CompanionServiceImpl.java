package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.PersonCompanion;
import com.personalapp.hometeaching.repository.AssignmentRepository;
import com.personalapp.hometeaching.repository.CompanionRepository;
import com.personalapp.hometeaching.repository.FamilyRepository;
import com.personalapp.hometeaching.repository.PersonCompanionRepository;
import com.personalapp.hometeaching.repository.PersonRepository;
import com.personalapp.hometeaching.service.CompanionService;
import com.personalapp.hometeaching.view.CompanionViewModel;
import com.personalapp.hometeaching.view.FamilyViewModel;

@Service
public class CompanionServiceImpl implements CompanionService {

	@Autowired
	private CompanionRepository repo;

	@Autowired
	private AssignmentRepository assignmentRepo;

	@Autowired
	private FamilyRepository familyRepo;

	@Autowired
	private PersonRepository personRepo;

	@Autowired
	private PersonCompanionRepository personCompanionRepo;

	@Override
	public CompanionViewModel addCompanion(Companion toCreate) {
		Companion companion = createNewCompanion(toCreate);
		return new CompanionViewModel(repo.findDetailedById(companion.getId()), true);
	}

	@Override
	public List<FamilyViewModel> addAssignment(Companion companion) {
		List<FamilyViewModel> families = newArrayList();
		for (Assignment assignment : companion.getAutopopulatingAssignments()) {
			saveAssignment(companion.getId(), assignment.getFamilyId());
			families.add(new FamilyViewModel(familyRepo.findDetailedById(assignment.getFamilyId()), true, false, true));
		}

		return families;
	}

	@Override
	public Boolean removeAssignment(Long companionId, Long familyId) {
		Assignment assignment = assignmentRepo.findByCompanionIdAndFamilyId(companionId, familyId);
		assignment.setActive(false);
		assignment.setUpdated(new Date());
		assignmentRepo.update(assignment);
		return true;
	}

	@Override
	public Boolean removeCompanion(Long companionId) {
		Companion companion = repo.findDetailedById(companionId);
		markAllInactive(companion);
		return true;
	}

	@Override
	public CompanionViewModel editAssignment(Companion toCreate) {
		Companion oldCompanion = repo.findDetailedById(toCreate.getId());
		// setup new companion with same families
		Companion companion = createNewCompanion(toCreate);

		for (Assignment assignment : oldCompanion.getAssignments()) {
			saveAssignment(companion.getId(), assignment.getFamilyId());
		}

		// mark old companion inactive
		markAllInactive(oldCompanion);
		return new CompanionViewModel(repo.findDetailedById(companion.getId()), true);
	}

	@Override
	public List<CompanionViewModel> getViewModelAllCompanionsAndActiveFamilies() {
		List<CompanionViewModel> companions = newArrayList();
		for (Companion companion : repo.getAllCompanionsAndActiveFamilies()) {
			companions.add(new CompanionViewModel(companion, true));
		}

		return companions;
	}

	@Override
	public List<Companion> getAllCompanionsAndActiveFamilies() {
		return repo.getAllCompanionsAndActiveFamilies();
	}

	@Override
	public Companion getCompanionAndActiveFamilies(Long companionId) {
		return repo.getCompanionAndActiveFamilies(companionId);
	}

	@Override
	public Companion findDetailedById(Long id) {
		Companion companion = repo.findDetailedById(id);
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

	private Assignment saveAssignment(Long companionId, Long familyId) {
		Assignment assignment = new Assignment();
		assignment.setCompanionId(companionId);
		assignment.setFamilyId(familyId);
		assignment.setCreated(new Date());
		assignment.setActive(true);
		assignmentRepo.save(assignment);
		return assignment;
	}

	private void markAllInactive(Companion companion) {
		companion.setActive(false);
		for (PersonCompanion personCompanion : companion.getCompanions()) {
			personCompanion.setActive(false);
		}
		for (Assignment assignment : companion.getAssignments()) {
			assignment.setActive(false);
		}
		repo.update(companion);
	}

	private Companion createNewCompanion(Companion toCreate) {
		Companion companion = new Companion();
		companion.setActive(true);
		Set<PersonCompanion> companions = newHashSet();
		for (PersonCompanion personCompanion : toCreate.getAutopopulatingPersonCompanions()) {
			personCompanion.setActive(true);
			personCompanion.setCompanion(companion);
			companions.add(personCompanion);
		}
		companion.setCompanions(companions);
		companion.setCreated(new Date());
		repo.save(companion);
		return companion;
	}
}
