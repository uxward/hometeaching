package com.personalapp.hometeaching.service;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.view.ActionViewModel;

public interface EmailService {

	ActionViewModel sendUpdatedAssignmentEmailToCompanion(Long companionId);

	ActionViewModel sendUpdatedAssignmentEmailToAllCompanions(Organization organization);

	ActionViewModel sendEmailToNewUser(Long hometeachingUserId);

	ActionViewModel sendTeachingReportEmailToAllCompanions(Organization organization);

	ActionViewModel sendForgotPasswordEmail(HometeachingUser user, String token);

	ActionViewModel sendTeachingReportEmailToCompanion(Long companionId);

	ActionViewModel sendVisitReminderEmailToAllCompanions(Organization fromId);

	ActionViewModel sendVisitReminderEmailToCompanion(Long companionId);
}
