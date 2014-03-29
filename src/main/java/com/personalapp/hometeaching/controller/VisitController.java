package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
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

import com.personalapp.hometeaching.model.Visit;
import com.personalapp.hometeaching.service.VisitService;
import com.personalapp.hometeaching.view.DatatableResponse;
import com.personalapp.hometeaching.view.VisitHistoryModel;
import com.personalapp.hometeaching.view.VisitViewModel;

@Controller
@RequestMapping(value = "/visit")
public class VisitController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private VisitService visitService;

	@RequestMapping("history/{n}")
	public ModelAndView viewHistory(@PathVariable Integer n) {
		List<String> months = visitService.getLastNMonths(n);
		return new ModelAndView("visit/history").addObject("months", months);
	}

	@RequestMapping("homeTeaching")
	@ResponseBody
	public DatatableResponse<VisitViewModel> getHomeTeachingVisits(@RequestParam("familyId") Long familyId) {
		logger.info("User {} is viewing home teaching visits for family id {} ", getCurrentUser().getUsername(), familyId );
		return new DatatableResponse<VisitViewModel>(visitService.getHomeTeachingVisitsByFamilyId(familyId));
	}

	@RequestMapping("visitingTeaching")
	@ResponseBody
	public DatatableResponse<VisitViewModel> getVisitingTeachingVisits(@RequestParam("familyId") Long familyId) {
		logger.info("User {} is viewing visiting teaching visits for family id {} ", getCurrentUser().getUsername(), familyId );
		return new DatatableResponse<VisitViewModel>(visitService.getVisitingTeachingVisitsByFamilyId(familyId));
	}

	@RequestMapping("getHistory")
	@ResponseBody
	public DatatableResponse<VisitHistoryModel> getHistory(@RequestParam("n") Integer n) {
		return new DatatableResponse<VisitHistoryModel>(visitService.getVisitHistory(n));
	}

	@RequestMapping(value = "/saveVisit")
	@ResponseBody
	public VisitViewModel saveVisit(Visit visit) {
		logger.info("User {} is saving a visit for family id {} and assignment id {}.", getCurrentUser().getUsername(), visit.getFamilyId(), visit.getAssignmentId());
		return visitService.save(visit);
	}

}
