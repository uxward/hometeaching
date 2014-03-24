package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalapp.hometeaching.service.EmailService;
import com.personalapp.hometeaching.view.ActionViewModel;

@Controller
@RequestMapping(value = "/email")
public class EmailController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private EmailService service;

	@RequestMapping("allCompanions")
	@ResponseBody()
	public ActionViewModel sendEmailForAllCompanions() {
		logger.info("User {} is trying to send an email to all companions in their organizations", getCurrentUser().getPerson().getFullName());
		return service.sendUpdatedAssignmentEmailToAllCompanions();
	}

	@RequestMapping("byCompanion")
	@ResponseBody()
	public ActionViewModel sendEmailForCompanion(@RequestParam Long companionId) {
		logger.info("User {} is trying to send an email to the companion with id {}", getCurrentUser().getPerson().getFullName(), companionId);
		return service.sendUpdatedAssignmentEmailToCompanion(companionId);
	}

	@RequestMapping("newUser")
	@ResponseBody()
	public ActionViewModel sendEmailForNewUser(@RequestParam Long userId) {
		logger.info("User {} is trying to send an email to new user with id {}", getCurrentUser().getPerson().getFullName(), userId);
		return service.sendEmailToNewUser(userId);
	}
}
