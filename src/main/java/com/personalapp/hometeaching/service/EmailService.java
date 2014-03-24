package com.personalapp.hometeaching.service;

import com.personalapp.hometeaching.view.ActionViewModel;

public interface EmailService {

	ActionViewModel sendUpdatedAssignmentEmailToCompanion(Long companionId);

	ActionViewModel sendUpdatedAssignmentEmailToAllCompanions();

	ActionViewModel sendEmailToNewUser(Long hometeachingUserId);
}
