package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserAssignableRoles;
import static com.personalapp.hometeaching.security.SecurityUtils.hasFamilyAccess;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyStatus;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.service.FamilyService;
import com.personalapp.hometeaching.view.DatatableResponse;
import com.personalapp.hometeaching.view.FamilyViewModel;

@Controller
@RequestMapping(value = "/family")
public class FamilyController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private FamilyService service;

	@RequestMapping("/all")
	public ModelAndView viewAll() {
		logger.info("User {} is viewing the all families page", getCurrentUser().getPerson().getFullName());
		ModelAndView view = new ModelAndView("family/families");
		view.addObject("statuses", FamilyStatus.values());
		view.addObject("organizations", Organization.forDisplay());
		return view;
	}

	@RequestMapping(value = "getAllFamilies")
	@ResponseBody
	public DatatableResponse<FamilyViewModel> getAllFamilies() {
		logger.info("User {} is getting all families", getCurrentUser().getPerson().getFullName());
		return new DatatableResponse<FamilyViewModel>(service.getAllNotMovedFamilies());
	}

	@RequestMapping("/you")
	public ModelAndView viewYou() {
		logger.info("User {} is viewing their own family page", getCurrentUser().getPerson().getFullName());
		return getFamilyViewModel(getCurrentUser().getFamily().getId());
	}

	@RequestMapping(value = "/get")
	@ResponseBody
	public FamilyViewModel get(@RequestParam("familyId") Long familyId) {
		logger.info("User {} is getting family information for family id {}", getCurrentUser().getPerson().getFullName(), familyId);
		return service.findDetailedFamilyViewModelById(familyId);
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public FamilyViewModel save(Family family) {
		logger.info("User {} is saving the {} family", getCurrentUser().getPerson().getFullName(), family.getFamilyName());
		return service.save(family);
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public FamilyViewModel edit(Family family) {
		logger.info("User {} is editing the {} family", getCurrentUser().getPerson().getFullName(), family.getFamilyName());
		return service.edit(family);
	}

	@RequestMapping(value = "/detail/{id}")
	public ModelAndView detail(@PathVariable Long id) {
		logger.info("User {} is viewing the family detail page for family with id {}", getCurrentUser().getPerson().getFullName(), id);
		return getFamilyViewModel(id);
	}

	@RequestMapping(value = "/moved")
	public ModelAndView moved() {
		logger.info("User {} is viewing the moved families page", getCurrentUser().getPerson().getFullName());
		return new ModelAndView("family/moved");
	}

	@RequestMapping(value = "/getAllMovedFamilies")
	@ResponseBody
	public DatatableResponse<FamilyViewModel> getAllMovedFamilies() {
		logger.info("User {} is getting all moved families", getCurrentUser().getPerson().getFullName());
		return new DatatableResponse<FamilyViewModel>(service.getAllMovedFamilies());
	}

	@RequestMapping(value = "/unknown")
	public ModelAndView unknown() {
		logger.info("User {} is viewing the unknown families page", getCurrentUser().getPerson().getFullName());
		return new ModelAndView("family/unknown");
	}

	@RequestMapping(value = "/getAllUnknownFamilies")
	@ResponseBody
	public DatatableResponse<FamilyViewModel> getAllUnknownFamilies() {
		logger.info("User {} is getting all unknown families page", getCurrentUser().getPerson().getFullName());
		return new DatatableResponse<FamilyViewModel>(service.getAllUnknownFamilies());
	}

	private ModelAndView getFamilyViewModel(Long id) {
		ModelAndView view = new ModelAndView("family/detail");
		Family family = service.findDetailedFamilyById(id);
		if (hasFamilyAccess(family)) {
			view.addObject("statuses", FamilyStatus.values());
			view.addObject("organizations", Organization.forDisplay());
			view.addObject("family", service.getDetailedViewModelForFamily(family));
			view.addObject("roles", getCurrentUserAssignableRoles());
		} else {
			view = new ModelAndView("denied");
		}
		return view;
	}

}
