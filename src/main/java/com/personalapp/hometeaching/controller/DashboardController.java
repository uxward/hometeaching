package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.getAllOrganizationIds;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
		return new ModelAndView("dashboard/visitPercentage");
	}

	@RequestMapping("familyStatus")
	public ModelAndView viewFamilyStatus() {
		return new ModelAndView("dashboard/familyStatus");
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

	@RequestMapping("getFamilyStatus")
	@ResponseBody
	public List<WardFamilyStatusViewModel> getFamilyStatus() {
		logger.info("User {} is getting family status percentages", getCurrentUser().getPerson().getFullName());
		return service.getFamilyStatusPercentage(getAllOrganizationIds());
	}
}
