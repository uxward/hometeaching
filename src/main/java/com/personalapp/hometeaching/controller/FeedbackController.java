package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.model.Feedback;
import com.personalapp.hometeaching.model.Priority;
import com.personalapp.hometeaching.service.FeedbackService;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.DatatableResponse;
import com.personalapp.hometeaching.view.FeedbackViewModel;

@Controller
@RequestMapping(value = "/feedback")
public class FeedbackController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private FeedbackService service;

	@RequestMapping()
	public ModelAndView viewFeedback() {
		ModelAndView view = new ModelAndView("feedback/feedback");
		view.addObject("priorities", Priority.values());
		return view;
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public ActionViewModel saveFeedback(Feedback feedback) {
		logger.info("User {} is attempting to save feedback.", getCurrentUser().getUsername());
		return service.createFeedback(feedback);
	}

	@RequestMapping(value = "getAllFeedback")
	@ResponseBody
	public DatatableResponse<FeedbackViewModel> getAllFeedback() {
		return new DatatableResponse<FeedbackViewModel>(service.getAllFeedback());
	}
}
