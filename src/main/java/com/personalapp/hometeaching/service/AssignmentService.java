package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.AssignmentViewModel;

public interface AssignmentService {

	List<AssignmentViewModel> findActiveByCompanion(Long companionId);

	AssignmentViewModel add(Assignment assignment);

	ActionViewModel remove(Long id);
}
