package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.view.AssignmentViewModel;

public interface AssignmentService {

	List<AssignmentViewModel> findActiveByCompanion(Long companionId);
}
