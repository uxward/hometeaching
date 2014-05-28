package com.personalapp.hometeaching.service.impl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.ResetPassword;
import com.personalapp.hometeaching.security.PasswordUtils;
import com.personalapp.hometeaching.service.EmailService;
import com.personalapp.hometeaching.service.ForgotPasswordService;
import com.personalapp.hometeaching.service.HometeachingUserService;
import com.personalapp.hometeaching.service.ResetPasswordService;
import com.personalapp.hometeaching.view.ActionViewModel;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private HometeachingUserService userService;

	@Autowired
	private ResetPasswordService resetPasswordService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordUtils passwordUtils;

	@Override
	public ActionViewModel forgotPassword(String email) {
		String token = randomAlphanumeric(32);
		HometeachingUser user = userService.findDetailedByUsername(email);
		ResetPassword reset = new ResetPassword();
		reset.setToken(token);
		reset.setUser(user);
		resetPasswordService.save(reset);
		ActionViewModel status = emailService.sendForgotPasswordEmail(user, token);
		return status;
	}

	@Override
	public ActionViewModel resetPassword(String token, String password) {
		ResetPassword reset = resetPasswordService.findByToken(token);
		HometeachingUser user = reset.getUser();
		user.setPassword(passwordUtils.getPassword(password));
		userService.update(user);
		return null;
	}
}
