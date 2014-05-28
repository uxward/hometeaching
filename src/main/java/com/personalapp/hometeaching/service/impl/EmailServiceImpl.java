package com.personalapp.hometeaching.service.impl;

import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.io.messaging.EmailClient;
import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.service.CompanionService;
import com.personalapp.hometeaching.service.EmailService;
import com.personalapp.hometeaching.service.HometeachingUserService;
import com.personalapp.hometeaching.view.ActionViewModel;

@Service
public class EmailServiceImpl implements EmailService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private HometeachingUserService userService;

	@Autowired
	private CompanionService companionService;

	@Autowired
	private EmailClient emailClient;

	@Override
	public ActionViewModel sendUpdatedAssignmentEmailToCompanion(Long companionId) {
		ActionStatus status = SUCCESS;
		try {
			Companion companion = companionService.getCompanionAndActiveFamilies(companionId);
			List<HometeachingUser> users = userService.getCompanionsToEmail(companionId);
			emailClient.sendUpdatedAssignmentEmail(companion, users);
		} catch (Exception e) {
			status = ERROR;
			logger.error(format("Unexpected exception occured while trying to email companion with id %s: ", companionId), e);
		}
		return getViewModelFromStatus(status);
	}

	@Override
	public ActionViewModel sendUpdatedAssignmentEmailToAllCompanions(Organization organization) {
		ActionStatus status = SUCCESS;
		for (Companion companion : companionService.getAllCompanionsAndActiveFamilies(organization)) {
			try {
				List<HometeachingUser> users = userService.getCompanionsToEmail(companion.getId());
				emailClient.sendUpdatedAssignmentEmail(companion, users);
			} catch (Exception e) {
				status = ERROR;
				logger.error(format("Unexpected exception occured while trying to email companion with id %s: ", companion.getId()), e);
			}
		}
		return getViewModelFromStatus(status);
	}

	@Override
	public ActionViewModel sendTeachingReportEmailToCompanion(Long companionId) {
		ActionStatus status = SUCCESS;
		try {
			Companion companion = companionService.getCompanionAndActiveFamilies(companionId);
			List<HometeachingUser> users = userService.getCompanionsToEmail(companionId);
			emailClient.sendReportTeachingEmail(companion, users);
		} catch (Exception e) {
			status = ERROR;
			logger.error(format("Unexpected exception occured while trying to email companion with id %s: ", companionId), e);
		}
		return getViewModelFromStatus(status);
	}

	@Override
	public ActionViewModel sendTeachingReportEmailToAllCompanions(Organization organization) {
		ActionStatus status = SUCCESS;
		for (Companion companion : companionService.getAllCompanionsAndActiveFamilies(organization)) {
			if (companion.getAssignments().size() > 0) {
				try {
					List<HometeachingUser> users = userService.getCompanionsToEmail(companion.getId());
					emailClient.sendReportTeachingEmail(companion, users);
				} catch (Exception e) {
					status = ERROR;
					logger.error(format("Unexpected exception occured while trying to email companion with id %s: ", companion.getId()), e);
				}
			} else {
				status = ActionStatus.ERROR;
			}
		}
		return getViewModelFromStatus(status);
	}

	@Override
	public ActionViewModel sendEmailToNewUser(Long hometeachingUserId) {
		ActionStatus status = SUCCESS;
		try {
			emailClient.sendNewUserEmail(userService.getUserDetails(hometeachingUserId));
		} catch (Exception e) {
			status = ERROR;
			logger.error(format("Unexpected exception occurred while trying to email new user with id %s", hometeachingUserId), e);
		}
		return getViewModelFromStatus(status);
	}

	private ActionViewModel getViewModelFromStatus(ActionStatus status) {
		ActionViewModel model = new ActionViewModel();
		model.setActionStatus(status);
		return model;
	}

	@Override
	public ActionViewModel sendForgotPasswordEmail(HometeachingUser user, String token) {
		ActionStatus status = SUCCESS;
		try {
			emailClient.sendForgotPasswordEmail(user, token);
		} catch (Exception e) {
			status = ERROR;
			logger.error(format("Unexpected exception occurred while trying to email new user with id %s", user.getId()), e);
		}
		return getViewModelFromStatus(status);
	}

	@Override
	public ActionViewModel sendVisitReminderEmailToAllCompanions(Organization organization) {
		ActionStatus status = SUCCESS;
		for (Companion companion : companionService.getAllCompanionsAndActiveFamilies(organization)) {
			if (companion.getAssignments().size() > 0) {
				try {
					List<HometeachingUser> users = userService.getCompanionsToEmail(companion.getId());
					emailClient.sendVisitReminderEmail(companion, users);
				} catch (Exception e) {
					status = ERROR;
					logger.error(format("Unexpected exception occured while trying to email companion with id %s: ", companion.getId()), e);
				}
			} else {
				status = ActionStatus.ERROR;
			}
		}
		return getViewModelFromStatus(status);
	}

	@Override
	public ActionViewModel sendVisitReminderEmailToCompanion(Long companionId) {
		ActionStatus status = SUCCESS;
		try {
			Companion companion = companionService.getCompanionAndActiveFamilies(companionId);
			List<HometeachingUser> users = userService.getCompanionsToEmail(companionId);
			emailClient.sendVisitReminderEmail(companion, users);
		} catch (Exception e) {
			status = ERROR;
			logger.error(format("Unexpected exception occured while trying to email companion with id %s: ", companionId), e);
		}
		return getViewModelFromStatus(status);
	}
}
