package com.personalapp.hometeaching.controller;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.service.ForgotPasswordService;
import com.personalapp.hometeaching.view.ActionViewModel;

@Controller
@RequestMapping(value = "/recover")
public class RecoverController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@RequestMapping()
	public ModelAndView forgotPassword() {
		return new ModelAndView("recover/forgot");
	}

	@RequestMapping("/request")
	@ResponseBody()
	public ActionViewModel request(@RequestParam String email) {
		logger.info("Anonymous user is trying to recover password for user with email {}", email);
		return forgotPasswordService.forgotPassword(email);
	}

	@RequestMapping("/reset/{token}")
	@ResponseBody()
	public ModelAndView reset(@PathVariable String token) {
		logger.info("Anonymous user is trying to reset password for user with token {}", token);
		return new ModelAndView("recover/reset", "token", token);
	}

	@RequestMapping("update")
	@ResponseBody()
	public ActionViewModel update(@RequestParam String token, @RequestParam String password) {
		logger.info("Anonymous user is trying to update password for user with token {}", token);
		return forgotPasswordService.resetPassword(token, password);
	}
}
