package com.personalapp.hometeaching.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.service.DashboardService;
import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.VisitPercentageViewModel;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {

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
		return service.getFamilyStatusPercentage();
	}

	@RequestMapping("getFamilyStatusPercentageByOrganization")
	@ResponseBody
	public List<FamilyStatusViewModel> getFamilyStatusPercentageByOrganization() {
		// TODO get ward and organization charts of family status
		return service.getFamilyStatusPercentage();
	}
}
