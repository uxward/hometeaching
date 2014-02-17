package com.personalapp.hometeaching.controller;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.personalapp.hometeaching.service.HometeachingUserService;
import com.personalapp.hometeaching.view.UserViewModel;

@Controller
@RequestMapping(value = "/email")
public class EmailController {
	private final Logger logger = getLogger(getClass());

	@Value("#{email.hostname}")
	private static String hostname;

	@Value("#{email.smtpPort}")
	private static Integer smtpPort;

	@Value("#{email.username}")
	private static String username;

	@Value("#{email.password}")
	private static String password;

	@Value("#{email.fromEmail}")
	private static String fromEmail;

	@Value("#{email.fromName}")
	private static String fromName;

	@Autowired
	private HometeachingUserService userService;

	@RequestMapping()
	public void sendEmail() {
		for (UserViewModel user : userService.getAllUsers()) {
			if (isNotEmpty(user.getEmail())) {
				try {
					HtmlEmail email = new HtmlEmail();
					email.setHostName(hostname);
					email.setSmtpPort(smtpPort);
					email.setAuthenticator(new DefaultAuthenticator(username, password));
					email.setSSL(true);
					email.setFrom(fromEmail, fromName);
					email.setSubject("Your updated home teaching assignment");
					email.setHtmlMsg("These are the updates");
					email.setTextMsg("This is a test mail ... :-)");
					email.addTo(user.getEmail(), "user.getName()");
					email.send();
				} catch (Exception e) {
					logger.error("Exception sending email {}", e);
				}
			}
		}
	}
}
