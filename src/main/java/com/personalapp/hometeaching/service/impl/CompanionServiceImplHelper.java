package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Sets.newHashSet;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.PersonCompanion;
import com.personalapp.hometeaching.repository.CompanionRepository;
import com.personalapp.hometeaching.service.AssignmentService;

@Service
public class CompanionServiceImplHelper {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private CompanionRepository repo;

	@Autowired
	private AssignmentService assignmentService;

	@Transactional(rollbackFor = Exception.class)
	public void doCreateNewCompanion(Companion companion) {
		companion.setActive(true);
		Set<PersonCompanion> companions = newHashSet();
		for (PersonCompanion personCompanion : companion.getAutopopulatingPersonCompanions()) {
			if (personCompanion.getPersonId() != null) {
				personCompanion.setActive(true);
				personCompanion.setCompanion(companion);
				companions.add(personCompanion);
			}
		}
		companion.setCompanions(companions);
		repo.save(companion);
	}

	@Transactional(rollbackFor = Exception.class)
	public void doEditCompanion(Companion companion) {
		Companion oldCompanion = repo.findDetailedById(companion.getId());

		companion.setId(null);
		doCreateNewCompanion(companion);

		for (Assignment assignment : oldCompanion.getAssignments()) {
			if (assignment.getActive()) {
				Assignment newAssignment = new Assignment();
				newAssignment.setActive(true);
				newAssignment.setFamilyId(assignment.getFamilyId());
				newAssignment.setCompanionId(assignment.getCompanionId());
				assignmentService.add(newAssignment);
			}
		}

		// mark old companion inactive
		markAllInactive(oldCompanion);
	}

	@Transactional(rollbackFor = Exception.class)
	public void doRemoveCompanion(Long companionId) {
		Companion companion = repo.findDetailedById(companionId);
		markAllInactive(companion);
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
}
