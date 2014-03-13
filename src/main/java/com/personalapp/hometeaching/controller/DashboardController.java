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
import com.personalapp.hometeaching.view.SummaryStatisticsViewModel;
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
		return new ModelAndView("dashboard/summaryStatistics");
	}

	@RequestMapping("getVisitPercentage")
	@ResponseBody
	public List<VisitPercentageViewModel> getVisitPercentage() {
		logger.info("User {} is getting visit percentages", getCurrentUser().getPerson().getFullName());
		return service.getVisitPercentage();
	}

	@RequestMapping("getVisitPercentageDetails")
	@ResponseBody
	public List<FamilyStatusViewModel> getVisitPercentageDetails(@RequestParam("month") Integer month, @RequestParam("year") Integer year) {
		logger.info("User {} is getting visit percentage details", getCurrentUser().getPerson().getFullName());
		return service.getVisitPercentageDetails(month, year);
	}

	@RequestMapping("getFamilyStatus")
	@ResponseBody
	public List<WardFamilyStatusViewModel> getFamilyStatus() {
		logger.info("User {} is getting family status percentages", getCurrentUser().getPerson().getFullName());
		return service.getFamilyStatusPercentage(getAllOrganizationIds());
	}

	@RequestMapping("getSummaryStatistics")
	@ResponseBody
	public List<SummaryStatisticsViewModel> getSummaryStatistics() {
		logger.info("User {} is getting summary statistics", getCurrentUser().getPerson().getFullName());
		return null;
	}
}
