package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.repository.AssignmentRepository;
import com.personalapp.hometeaching.service.AssignmentService;
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
}
