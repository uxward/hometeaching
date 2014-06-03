package com.personalapp.hometeaching.service.impl;

import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.NOT_FOUND;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;
import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.HashSet;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Recovery;
import com.personalapp.hometeaching.model.UserOrganization;
import com.personalapp.hometeaching.model.UserRole;
import com.personalapp.hometeaching.security.PasswordUtils;
import com.personalapp.hometeaching.service.EmailService;
import com.personalapp.hometeaching.service.ForgotPasswordService;
import com.personalapp.hometeaching.service.HometeachingUserService;
import com.personalapp.hometeaching.service.RecoveryService;
import com.personalapp.hometeaching.view.ActionViewModel;

@Service
public class RecoveryServiceImpl extends ServiceImpl implements ForgotPasswordService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private HometeachingUserService userService;

	@Autowired
	private RecoveryService resetPasswordService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordUtils passwordUtils;

	@Override
	public ActionViewModel forgotPassword(String email) {
		ActionStatus status = SUCCESS;
		try {
			HometeachingUser user = getRecoveryUser(email);
			if (user != null) {
				setupRecovery(user);
			} else {
				status = NOT_FOUND;
			}
		} catch (Exception e) {
			logger.error(format("An unexpected error occurred while setting up forgot password for user with email %s: ", email), e);
			status = ERROR;
		}

		return setupActionViewModel(status);
	}

	@Override
	public ActionViewModel resetPassword(String token, String password) {
		ActionStatus status = SUCCESS;
		try {
			Recovery reset = resetPasswordService.findByToken(token);
			if (reset != null) {
				doResetPassword(password, reset);
			} else {
				status = NOT_FOUND;
			}
		} catch (Exception e) {
			logger.error(format("An unexpected error occurred while setting up forgot password for user with token %s: ", token), e);
			status = ERROR;
		}

		return setupActionViewModel(status);
	}

	private HometeachingUser getRecoveryUser(String email) {
		HometeachingUser user = userService.findDetailedByUsername(email);
		if (user == null) {
			user = userService.findDetailedByEmail(email);
		}
		return user;
	}

	private void setupRecovery(HometeachingUser user) {
		Recovery reset = new Recovery();
		String token = randomAlphanumeric(32);
		reset.setToken(token);
		reset.setUser(user);
		resetPasswordService.save(reset);
		emailService.sendForgotPasswordEmail(user, token);
	}

	private void doResetPassword(String password, Recovery reset) {
		HometeachingUser user = reset.getUser();
		user.setPassword(password);
		setupUserForReset(user);
		userService.update(user);
		resetPasswordService.remove(reset);
	}

	private void setupUserForReset(HometeachingUser user) {
		for (UserRole role : user.getUserRoles()) {
			user.getUserRoleIds().add(role.getRole());
		}
		user.setUserRoles(new HashSet<UserRole>());
		for (UserOrganization org : user.getUserOrganizations()) {
			user.getUserOrganizationIds().add(org.getId());
		}
		user.setUserOrganizations(new HashSet<UserOrganization>());

	}
}
