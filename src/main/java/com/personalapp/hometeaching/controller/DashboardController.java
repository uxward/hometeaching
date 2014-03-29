package com.personalapp.hometeaching.controller;

import static com.google.common.collect.Lists.newArrayList;
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

import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.service.DashboardService;
import com.personalapp.hometeaching.view.DatatableResponse;
import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.SummaryStatisticsViewModel;
import com.personalapp.hometeaching.view.WardFamilyStatusViewModel;
import com.personalapp.hometeaching.view.WardVisitPercentageViewModel;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private DashboardService service;

	@RequestMapping("visitPercentage")
	public ModelAndView viewVisitPercentage() {
		logger.info("User {} is viewing the visit percentage page", getCurrentUser().getPerson().getFullName());
		return new ModelAndView("dashboard/visitPercentage");
	}

	@RequestMapping("familyStatus")
	public ModelAndView viewFamilyStatus() {
		logger.info("User {} is viewing the family status page", getCurrentUser().getPerson().getFullName());
		return new ModelAndView("dashboard/familyStatus");
	}

	@RequestMapping("summaryStatistics")
	public ModelAndView viewSummaryStatistics() {
		logger.info("User {} is viewing the summary statistics page", getCurrentUser().getPerson().getFullName());
		ModelAndView view = new ModelAndView("dashboard/summaryStatistics");
		view.addObject("organizations", Organization.values());
		return view;
	}

	@RequestMapping("getVisitPercentage")
	@ResponseBody
	public List<WardVisitPercentageViewModel> getVisitPercentage() {
		logger.info("User {} is getting visit percentages", getCurrentUser().getPerson().getFullName());
		return service.getVisitPercentage(getAllOrganizationIds());
	}

	@RequestMapping("getVisitPercentageDetails")
	@ResponseBody
	public List<FamilyStatusViewModel> getVisitPercentageDetails(@RequestParam("month") Integer month, @RequestParam("year") Integer year, @RequestParam("organizationId") Long organizationId) {
		logger.info("User {} is getting visit percentage details", getCurrentUser().getPerson().getFullName());
		return service.getVisitPercentageDetails(month, year, organizationId);
	}

	@RequestMapping("getFamilyStatus")
	@ResponseBody
	public List<WardFamilyStatusViewModel> getFamilyStatus() {
		logger.info("User {} is getting family status percentages", getCurrentUser().getPerson().getFullName());
		return service.getFamilyStatusPercentage(getAllOrganizationIds());
	}

	@RequestMapping("getFamilyStatusByOrganizationId")
	@ResponseBody
	public DatatableResponse<FamilyStatusViewModel> getFamilyStatusByOrganizationId(@RequestParam("organizationId") Long organizationId) {
		logger.info("User {} is getting family status percentages", getCurrentUser().getPerson().getFullName());
		List<WardFamilyStatusViewModel> status = service.getFamilyStatusPercentage(newArrayList(organizationId));
		return new DatatableResponse<FamilyStatusViewModel>(status.get(0).getFamilyStatus());
	}

	@RequestMapping("getSummaryStatistics")
	@ResponseBody
	public List<SummaryStatisticsViewModel> getSummaryStatistics() {
		logger.info("User {} is getting summary statistics", getCurrentUser().getPerson().getFullName());
		return null;
	}
}
