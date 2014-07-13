package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.model.Organization.fromId;
import static com.personalapp.hometeaching.model.Organization.isVisitingTeaching;
import static com.personalapp.hometeaching.security.SecurityUtils.canActionCompanion;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserAssignableRoles;
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
import com.personalapp.hometeaching.model.Organization;
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

	@RequestMapping("all/{organizationId}")
	public ModelAndView viewAllCompanionsByOrganization(@PathVariable Long organizationId) {
		Organization organization = fromId(organizationId);
		boolean visitingTeaching = isVisitingTeaching(organization);
		ModelAndView view = new ModelAndView("companion/teachers");
		view.addObject("teachers", personService.getAllNotMovedTeachers(visitingTeaching));
		view.addObject("organization", organization).addObject("visitingTeaching", visitingTeaching);
		return view;
	}

	@RequestMapping("getAll/{organizationId}")
	@ResponseBody
	public DatatableResponse<CompanionViewModel> getAllCompanionsByOrganization(@PathVariable Long organizationId) {
		Organization organization = fromId(organizationId);
		return new DatatableResponse<CompanionViewModel>(service.getViewModelAllCompanionsAndActiveFamiliesByOrganization(organization));
	}

	@RequestMapping("you/{visitingTeaching}")
	public ModelAndView viewYourCompanions(@PathVariable boolean visitingTeaching) {
		logger.info("User {} is viewing their {} teaching information.", getCurrentUser().getUsername(), visitingTeaching ? "visiting" : "home");
		ModelAndView view;
		List<Companion> companions = getCurrentUser().getActiveCompanions(visitingTeaching);
		if (companions.size() == 1) {
			view = getDetailModelAndView(service.findDetailedById(companions.get(0).getId()));
		} else if (companions.size() > 1) {
			view = getLandingModelAndView(getCurrentUser().getHometeachingUser(), companions, visitingTeaching);
		} else {
			view = new ModelAndView("companion/notAssigned");
		}

		return view;
	}

	@RequestMapping(value = "detail/{id}")
	public ModelAndView viewCompanionDetail(@PathVariable Long id) {
		logger.info("User {} is viewing the companion information of companion with id {}.", getCurrentUser().getUsername(), id);
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

	@RequestMapping(value = "getByPerson/{visitingTeaching}")
	@ResponseBody
	public DatatableResponse<CompanionViewModel> getusersCompanions(@RequestParam("personId") Long personId, @PathVariable boolean visitingTeaching) {
		return new DatatableResponse<CompanionViewModel>(service.getDetailedCompanionViewModelsByPersonId(personId, visitingTeaching));
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
		return service.editCompanion(companion);
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
		ModelAndView view = new ModelAndView("companion/teachingDetail");
		view.addObject("families", familyService.getAllFamiliesWithoutCompanion(companion.isVisitingTeaching()));
		view.addObject("companion", service.getDetailedViewModelForCompanion(companion));
		view.addObject("canAction", canActionCompanion(companion));
		view.addObject("visitingTeaching", companion.isVisitingTeaching());
		view.addObject("roles", getCurrentUserAssignableRoles());
		return view;
	}

	private ModelAndView getLandingModelAndView(HometeachingUser user, List<Companion> companions, boolean visitingTeaching) {
		ModelAndView view;
		view = new ModelAndView("companion/teachingLanding");
		view.addObject("person", personService.findDetailedViewModelById(user.getPerson().getId())).addObject("visitingTeaching", visitingTeaching);
		return view;
	}
}
