package com.personalapp.hometeaching.controller;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.service.AssignmentService;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.AssignmentViewModel;
import com.personalapp.hometeaching.view.DatatableResponse;

@Controller
@RequestMapping(value = "/assignment")
public class AssignmentController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private AssignmentService service;

	@RequestMapping(value = "getAssignments/{companionId}")
	@ResponseBody
	public DatatableResponse<AssignmentViewModel> getAssignments(@PathVariable Long companionId) {
		return new DatatableResponse<AssignmentViewModel>(service.findActiveByCompanion(companionId));
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public AssignmentViewModel addAssignment(Assignment assignment) {
		return service.add(assignment);
	}

	@RequestMapping(value = "/remove/{assignmentId}")
	@ResponseBody
	public ActionViewModel removeAssignment(@PathVariable Long assignmentId) {
		return service.remove(assignmentId);
	}
}
