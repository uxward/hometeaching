package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.model.Organization.fromId;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping("updatedAssignment/{organizationId}")
	@ResponseBody()
	public ActionViewModel sendEmailForUpdatedAssignment(@PathVariable Long organizationId) {
		logger.info("User {} is trying to send an updated assignment email to all companions in organization with id {}", getCurrentUser().getPerson().getFullName(), organizationId);
		return service.sendUpdatedAssignmentEmailToAllCompanions(fromId(organizationId));
	}

	@RequestMapping("updatedAssignment")
	@ResponseBody()
	public ActionViewModel sendEmailForUpdatedAssignmentByCompanion(@RequestParam Long companionId) {
		logger.info("User {} is trying to send an updated assignment email to the companion with id {}", getCurrentUser().getPerson().getFullName(), companionId);
		return service.sendUpdatedAssignmentEmailToCompanion(companionId);
	}

	@RequestMapping("reportUpdate/{organizationId}")
	@ResponseBody()
	public ActionViewModel sendEmailForReportUpdate(@PathVariable Long organizationId) {
		logger.info("User {} is trying to send a report update email to all companions in organization with id {}", getCurrentUser().getPerson().getFullName(), organizationId);
		return service.sendTeachingReportEmailToAllCompanions(fromId(organizationId));
	}

	@RequestMapping("reportUpdate")
	@ResponseBody()
	public ActionViewModel sendEmailForReportUpdateByCompanion(@RequestParam Long companionId) {
		logger.info("User {} is trying to send a report update email to companion  with id {}", getCurrentUser().getPerson().getFullName(), companionId);
		return service.sendTeachingReportEmailToCompanion(companionId);
	}

	@RequestMapping("newUser")
	@ResponseBody()
	public ActionViewModel sendEmailForNewUser(@RequestParam Long userId) {
		logger.info("User {} is trying to send an email to new user with id {}", getCurrentUser().getPerson().getFullName(), userId);
		return service.sendEmailToNewUser(userId);
	}

	@RequestMapping("visitReminder/{organizationId}")
	@ResponseBody()
	public ActionViewModel sendEmailForVisitReminder(@PathVariable Long organizationId) {
		logger.info("User {} is trying to send a visit reminder email to all companions for organization with id {}", getCurrentUser().getPerson().getFullName(), organizationId);
		return service.sendVisitReminderEmailToAllCompanions(fromId(organizationId));
	}

	@RequestMapping("visitReminder")
	@ResponseBody()
	public ActionViewModel sendEmailForVisitReminderByCompanion(@RequestParam Long companionId) {
		logger.info("User {} is trying to send a visit reminder email to companion with id {}", getCurrentUser().getPerson().getFullName(), companionId);
		return service.sendVisitReminderEmailToCompanion(companionId);
	}
}
