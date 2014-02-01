package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.currentUserIsAdmin;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static com.personalapp.hometeaching.security.SecurityUtils.hasFamilyAccess;

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

	@Autowired
	private FamilyService service;

	@RequestMapping("/all")
	public ModelAndView viewAll() {
		ModelAndView view = new ModelAndView("family/families");
		view.addObject("statuses", FamilyStatus.values());
		view.addObject("organizations", Organization.values());
		return view;
	}

	@RequestMapping(value = "getAllFamilies")
	@ResponseBody
	public DatatableResponse<FamilyViewModel> getAllFamilies() {
		return new DatatableResponse<FamilyViewModel>(service.getAllFamilies());
	}

	@RequestMapping("/you")
	public ModelAndView viewYou() {
		ModelAndView view = getFamilyViewModel(getCurrentUser().getFamily().getId());
		return view;
	}

	@RequestMapping(value = "/get")
	@ResponseBody
	public FamilyViewModel get(@RequestParam("familyId") Long familyId) {
		return service.findDetailedFamilyViewModelById(familyId);
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public FamilyViewModel save(Family family) {
		return service.save(family);
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public FamilyViewModel edit(Family family) {
		return service.edit(family);
	}

	@RequestMapping(value = "/detail/{id}")
	public ModelAndView detail(@PathVariable Long id) {
		ModelAndView view = getFamilyViewModel(id);

		return view;
	}

	private ModelAndView getFamilyViewModel(Long id) {
		ModelAndView view = new ModelAndView("family/detail");
		Family family = service.findDetailedFamilyById(id);
		if (currentUserIsAdmin() || hasFamilyAccess(family)) {
			view.addObject("statuses", FamilyStatus.values());
			view.addObject("organizations", Organization.values());
			view.addObject("family", service.getDetailedViewModelForFamily(family));
		} else {
			view = new ModelAndView("denied");
		}
		return view;
	}

}
