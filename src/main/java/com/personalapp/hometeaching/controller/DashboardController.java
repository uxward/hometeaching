package com.personalapp.hometeaching.controller;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserOrganizationIds;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.security.SecurityUtils;
import com.personalapp.hometeaching.service.DashboardService;
import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.VisitPercentageViewModel;
import com.personalapp.hometeaching.view.WardFamilyStatusViewModel;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private DashboardService service;

	@RequestMapping("visitPercentage")
	public ModelAndView viewVisitPercentage() {
		ModelAndView view = new ModelAndView("dashboard/visitPercentage");
		return view;
	}

	@RequestMapping("getVisitPercentage")
	@ResponseBody
	public List<VisitPercentageViewModel> getVisitPercentage() {
		return service.getVisitPercentage();
	}

	@RequestMapping("getVisitPercentageDetails")
	@ResponseBody
	public List<FamilyStatusViewModel> getVisitPercentageDetails(@RequestParam("month") Integer month, @RequestParam("year") Integer year) {
		return service.getVisitPercentageDetails(month, year);
	}

	@RequestMapping("getFamilyStatusPercentage")
	@ResponseBody
	public List<FamilyStatusViewModel> getFamilyStatusPercentage() {
		return service.getFamilyStatusPercentage(getCurrentUserOrganizationIds());
	}

	@RequestMapping("getFamilyStatusPercentageByOrganization")
	@ResponseBody
	public WardFamilyStatusViewModel getFamilyStatusPercentageByOrganization(@RequestParam("organizationId") Long organizationId) {
		logger.info("User " + SecurityUtils.getCurrentUser().getPerson().getFullName() + " is getting family status percentages for organization with id " + organizationId);
		List<FamilyStatusViewModel> statuses = service.getFamilyStatusPercentage(newArrayList(organizationId));
		return new WardFamilyStatusViewModel(statuses, Organization.fromId(organizationId));
	}
}
