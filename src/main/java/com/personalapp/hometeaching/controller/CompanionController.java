package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.canActionCompanion;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static com.personalapp.hometeaching.security.SecurityUtils.hasCompanionAccess;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.service.CompanionService;
import com.personalapp.hometeaching.service.FamilyService;
import com.personalapp.hometeaching.service.PersonService;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.CompanionViewModel;
import com.personalapp.hometeaching.view.DatatableResponse;
import com.personalapp.hometeaching.view.FamilyViewModel;

@Controller
@RequestMapping(value = "/companion")
public class CompanionController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private CompanionService service;

	@Autowired
	private FamilyService familyService;

	@Autowired
	private PersonService personService;

	@RequestMapping("all")
	public ModelAndView viewAll() {
		ModelAndView view = new ModelAndView("companion/companions");
		view.addObject("hometeachers", personService.getAllNotMovedNotAssignedHometeachers());
		return view;
	}

	@RequestMapping("getCompanions")
	@ResponseBody
	public DatatableResponse<CompanionViewModel> getCompanions() {
		return new DatatableResponse<CompanionViewModel>(service.getViewModelAllCompanionsAndActiveFamilies());
	}

	@RequestMapping("you")
	public ModelAndView viewYou() {
		logger.info("User {} is viewing their companion information.", getCurrentUser().getUsername());
		ModelAndView view;
		if (getCurrentUser().getActiveCompanion() != null) {
			view = getDetailModelAndView(service.findDetailedById(getCurrentUser().getActiveCompanion().getId()));
		} else {
			view = new ModelAndView("companion/notAssigned");
		}

		return view;
	}

	@RequestMapping(value = "/detail/{id}")
	public ModelAndView detail(@PathVariable Long id) {
		ModelAndView view;
		Companion companion = service.findDetailedById(id);
		if (hasCompanionAccess(companion)) {
			view = getDetailModelAndView(companion);
		} else {
			view = new ModelAndView("denied");
		}
		return view;
	}

	@RequestMapping(value = "getAssignments")
	@ResponseBody
	public DatatableResponse<FamilyViewModel> getAssignments(@RequestParam("companionId") Long companionId) {

		return new DatatableResponse<FamilyViewModel>(familyService.getByCompanionId(companionId));
	}

	@RequestMapping("/save")
	@ResponseBody
	public CompanionViewModel save(Companion companion) {
		return service.addCompanion(companion);
	}

	@RequestMapping("/remove")
	@ResponseBody
	public ActionViewModel remove(Long companionId) {
		return service.removeCompanion(companionId);
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public CompanionViewModel edit(Companion companion) {
		return service.editAssignment(companion);
	}

	@RequestMapping(value = "/addAssignment")
	@ResponseBody
	public FamilyViewModel addAssignment(Companion companion) {
		return service.addAssignment(companion);
	}

	@RequestMapping(value = "/removeAssignment")
	@ResponseBody
	public ActionViewModel removeAssignment(Long companionId, Long familyId) {
		return service.removeAssignment(companionId, familyId);
	}

	private ModelAndView getDetailModelAndView(Companion companion) {
		ModelAndView view = new ModelAndView("companion/detail");
		view.addObject("companion", service.getDetailedViewModelForCompanion(companion));
		view.addObject("families", familyService.getAllFamiliesWithoutCompanion());
		view.addObject("canAction", canActionCompanion(companion));
		return view;
	}
}
