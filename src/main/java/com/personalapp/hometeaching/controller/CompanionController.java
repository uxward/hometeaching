package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.canActionCompanion;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static com.personalapp.hometeaching.security.SecurityUtils.hasCompanionAccess;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.HometeachingUser;
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

	@RequestMapping("allHomeTeachers")
	public ModelAndView viewAllHomeTeachers() {
		ModelAndView view = new ModelAndView("companion/homeTeachers");
		view.addObject("teachers", personService.getAllNotMovedHomeTeachers());
		return view;
	}

	@RequestMapping("allVisitingTeachers")
	public ModelAndView viewAllVisitingTeachers() {
		ModelAndView view = new ModelAndView("companion/visitingTeachers");
		view.addObject("teachers", personService.getAllNotMovedVisitingTeachers());
		return view;
	}

	@RequestMapping("getHomeTeachingCompanions")
	@ResponseBody
	public DatatableResponse<CompanionViewModel> getHomeTeachingCompanions() {
		return new DatatableResponse<CompanionViewModel>(service.getViewModelAllHomeTeachingCompanionsAndActiveFamilies());
	}

	@RequestMapping("getVisitingTeachingCompanions")
	@ResponseBody
	public DatatableResponse<CompanionViewModel> getVisitingTeachingCompanions() {
		return new DatatableResponse<CompanionViewModel>(service.getViewModelAllVisitingTeachingCompanionsAndActiveFamilies());
	}

	@RequestMapping("yourHomeTeaching")
	public ModelAndView viewYourHomeTeaching() {
		logger.info("User {} is viewing their home teaching information.", getCurrentUser().getUsername());
		ModelAndView view;
		List<Companion> companions = getCurrentUser().getActiveHomeTeachingCompanions();
		if (companions.size() == 1) {
			view = getDetailHomeTeachingModelAndView(service.findDetailedById(companions.get(0).getId()));
		} else if (companions.size() > 1) {
			view = getLandingHomeTeachingModelAndView(getCurrentUser().getHometeachingUser(), companions);
		} else {
			view = new ModelAndView("companion/notAssigned");
		}

		return view;
	}

	@RequestMapping("yourVisitingTeaching")
	public ModelAndView viewYourVisitingTeaching() {
		logger.info("User {} is viewing their visiting teaching information.", getCurrentUser().getUsername());
		ModelAndView view;
		List<Companion> companions = getCurrentUser().getActiveVisitingTeachingCompanions();
		if (companions.size() == 1) {
			view = getDetailVisitingTeachingModelAndView(service.findDetailedById(companions.get(0).getId()));
		} else if (companions.size() > 1) {
			view = getLandingVisitingTeachingModelAndView(getCurrentUser().getHometeachingUser(), companions);
		} else {
			view = new ModelAndView("companion/notAssigned");
		}

		return view;
	}

	@RequestMapping(value = "/homeTeachingDetail/{id}")
	public ModelAndView homeTeachingDetail(@PathVariable Long id) {
		logger.info("User {} is viewing the companion information of companion with id {}.", getCurrentUser().getUsername(), id);
		ModelAndView view;

		Companion companion = service.findDetailedById(id);
		if (hasCompanionAccess(companion)) {
			view = getDetailHomeTeachingModelAndView(companion);
		} else {
			view = new ModelAndView("denied");
		}
		return view;
	}

	@RequestMapping(value = "/visitingTeachingDetail/{id}")
	public ModelAndView visitingTeachingDetail(@PathVariable Long id) {
		logger.info("User {} is viewing the companion information of companion with id {}.", getCurrentUser().getUsername(), id);
		ModelAndView view;

		Companion companion = service.findDetailedById(id);
		if (hasCompanionAccess(companion)) {
			view = getDetailVisitingTeachingModelAndView(companion);
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

	@RequestMapping(value = "getAllHomeTeachingCompanions")
	@ResponseBody
	public DatatableResponse<CompanionViewModel> getAllHomeTeachingCompanions(@RequestParam("personId") Long personId) {
		return new DatatableResponse<CompanionViewModel>(service.getDetailedHomeTeachingViewModelsByPersonId(personId));
	}

	@RequestMapping(value = "getAllVisitingTeachingCompanions")
	@ResponseBody
	public DatatableResponse<CompanionViewModel> getAllVisitingTeachingCompanions(@RequestParam("personId") Long personId) {
		return new DatatableResponse<CompanionViewModel>(service.getDetailedVisitingTeachingViewModelsByPersonId(personId));
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

	private ModelAndView getDetailHomeTeachingModelAndView(Companion companion) {
		ModelAndView view = new ModelAndView("companion/homeTeachingDetail");
		view.addObject("companion", service.getDetailedViewModelForCompanion(companion));
		view.addObject("families", familyService.getAllFamiliesWithoutHomeTeachingCompanion());
		view.addObject("canAction", canActionCompanion(companion));
		return view;
	}

	private ModelAndView getDetailVisitingTeachingModelAndView(Companion companion) {
		ModelAndView view = new ModelAndView("companion/visitingTeachingDetail");
		view.addObject("companion", service.getDetailedViewModelForCompanion(companion));
		view.addObject("families", familyService.getAllFamiliesWithoutVisitingTeachingCompanion());
		view.addObject("canAction", canActionCompanion(companion));
		return view;
	}

	private ModelAndView getLandingVisitingTeachingModelAndView(HometeachingUser user, List<Companion> companions) {
		ModelAndView view = new ModelAndView("companion/visitingTeachingLanding");
		view.addObject("person", personService.findDetailedViewModelById(user.getPerson().getId()));
		return view;
	}

	private ModelAndView getLandingHomeTeachingModelAndView(HometeachingUser user, List<Companion> companions) {
		ModelAndView view = new ModelAndView("companion/homeTeachingLanding");
		view.addObject("person", personService.findDetailedViewModelById(user.getPerson().getId()));
		return view;
	}
}
