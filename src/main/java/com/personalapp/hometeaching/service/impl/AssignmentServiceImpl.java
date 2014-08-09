package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.repository.AssignmentRepository;
import com.personalapp.hometeaching.service.AssignmentService;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.AssignmentViewModel;

@Service
public class AssignmentServiceImpl implements AssignmentService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private AssignmentRepository repo;

	@Override
	public List<AssignmentViewModel> findActiveByCompanion(Long companionId) {
		List<AssignmentViewModel> assignments = newArrayList();
		for (Assignment assignment : repo.findActiveByCompanion(companionId)) {
			assignments.add(new AssignmentViewModel(assignment));
		}
		return assignments;
	}

	@CacheEvict(value = "visitHistory", allEntries = true)
	@Override
	public AssignmentViewModel add(Assignment assignment) {
		ActionStatus status = SUCCESS;
		try {
			assignment.setActive(true);
			repo.save(assignment);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to add the assignment: {}", e);
			status = ERROR;
		}
		return new AssignmentViewModel(repo.findDetailedById(assignment.getId()), status);
	}

	@CacheEvict(value = "visitHistory", allEntries = true)
	@Override
	public ActionViewModel remove(Long id) {
		ActionStatus status = SUCCESS;
		try {
			Assignment assignment = repo.findById(id);
			assignment.setActive(false);
			repo.update(assignment);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to remove the assignment: {}", e);
			status = ERROR;
		}
		ActionViewModel actionViewModel = new ActionViewModel();
		actionViewModel.setActionStatus(status);
		return actionViewModel;
	}
}
