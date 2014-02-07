package com.personalapp.hometeaching.controller;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/email")
public class EmailController {
	private final Logger logger = getLogger(getClass());

	@RequestMapping()
	public void sendEmail() {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("d4elders", "hometeaching"));
			email.setSSL(true);
			email.setFrom("d4elders@gmail.com", "D4 Elders");
			email.setSubject("TestMail");
			email.setHtmlMsg("<html><p>DUDE<i>DUDE</i></p></html>");
			email.setTextMsg("This is a test mail ... :-)");
			email.addTo("philman311@gmail.com", "Phillip");
			email.send();
		} catch (Exception e) {
			logger.error("Exception sending email", e);
		}
	}
}
