package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Sets.newHashSet;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.PersonCompanion;
import com.personalapp.hometeaching.repository.AssignmentRepository;
import com.personalapp.hometeaching.repository.CompanionRepository;

@Service
public class CompanionServiceImplHelper {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private CompanionRepository repo;

	@Autowired
	private AssignmentRepository assignmentRepo;

	@Transactional(rollbackFor = Exception.class)
	public void doCreateNewCompanion(Companion companion) {
		companion.setActive(true);
		Set<PersonCompanion> companions = newHashSet();
		for (PersonCompanion personCompanion : companion.getAutopopulatingPersonCompanions()) {
			personCompanion.setActive(true);
			personCompanion.setCompanion(companion);
			companions.add(personCompanion);
		}
		companion.setCompanions(companions);
		companion.setCreated(new Date());
		repo.save(companion);
	}

	@Transactional(rollbackFor = Exception.class)
	public void doEditCompanion(Companion companion) {
		Companion oldCompanion = repo.findDetailedById(companion.getId());

		doCreateNewCompanion(companion);

		for (Assignment assignment : oldCompanion.getAssignments()) {
			saveAssignment(companion.getId(), assignment.getFamilyId());
		}

		// mark old companion inactive
		markAllInactive(oldCompanion);
	}

	@Transactional(rollbackFor = Exception.class)
	public void doRemoveCompanion(Long companionId) {
		Companion companion = repo.findDetailedById(companionId);
		markAllInactive(companion);
	}

	@Transactional(rollbackFor = Exception.class)
	public void doAddAssignment(Companion companion) {
		saveAssignment(companion.getId(), companion.getAutopopulatingAssignments().get(0).getFamilyId());
	}

	@Transactional(rollbackFor = Exception.class)
	public void doRemoveAssignment(Long companionId, Long familyId) {
		Assignment assignment = assignmentRepo.findByCompanionIdAndFamilyId(companionId, familyId);
		assignment.setActive(false);
		assignment.setUpdated(new Date());
		assignmentRepo.update(assignment);
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

	private void saveAssignment(Long companionId, Long familyId) {
		Assignment assignment = new Assignment();
		assignment.setCompanionId(companionId);
		assignment.setFamilyId(familyId);
		assignment.setCreated(new Date());
		assignment.setActive(true);
		assignmentRepo.save(assignment);
	}
}
