package com.personalapp.hometeaching.controller;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Objects;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.model.PersonCompanion;
import com.personalapp.hometeaching.service.CompanionService;
import com.personalapp.hometeaching.service.HometeachingUserService;

@Controller
@RequestMapping(value = "/email")
public class EmailController {
	private final Logger logger = getLogger(getClass());

	@Value("${email.hostname}")
	private String hostname;

	@Value("${email.smtpPort}")
	private Integer smtpPort;

	@Value("${email.username}")
	private String username;

	@Value("${email.password}")
	private String password;

	@Value("${email.fromEmail}")
	private String fromEmail;

	@Value("${email.fromName}")
	private String fromName;

	@Autowired
	private HometeachingUserService userService;
	@Autowired
	private CompanionService companionService;

	@RequestMapping()
	@ResponseBody()
	public Boolean sendEmail() {
		List<Companion> companions = companionService.getAllCompanionsAndActiveFamilies();
		for (HometeachingUser user : userService.getAllUsersToEmail()) {
			if (isNotEmpty(user.getEmail())) {
				try {
					Companion userCompanion = null;
					for (Companion companion : companions) {
						if (Objects.equals(user.getPerson().getActiveCompanion().getCompanion().getId(), companion.getId())) {
							userCompanion = companion;
							break;
						}
					}
					Long personId = user.getPerson().getId();
					String toName = user.getPerson().getFirstName() + " " + user.getPerson().getFamily().getFamilyName();
					String companionName = null;
					for (PersonCompanion personCompanion : userCompanion.getCompanions()) {
						if (!personCompanion.getPerson().getId().equals(personId)) {
							companionName = personCompanion.getPerson().getFirstName() + " " + personCompanion.getPerson().getFamily().getFamilyName();
						}
					}
					StringBuilder htmlMessage = new StringBuilder();
					htmlMessage.append("<html><body><p>");
					htmlMessage.append(user.getPerson().getFirstName());
					htmlMessage.append(",<br/>Here is your most up to date home teaching assignment with ");
					htmlMessage.append(companionName);
					htmlMessage.append(":<br/><ul>");
					for (Assignment assignment : userCompanion.getAssignments()) {
						if (assignment.getActive()) {
							htmlMessage.append("<li>");
							String assignedFamily = null;
							for (Person person : assignment.getFamily().getPeople()) {
								if (person.getHeadOfHousehold()) {
									if (assignedFamily == null) {
										assignedFamily = person.getFirstName();
									} else {
										assignedFamily += " and " + person.getFirstName();
									}
								}
							}
							assignedFamily += " " + assignment.getFamily().getFamilyName();
							htmlMessage.append(assignedFamily);
							htmlMessage.append("</li>");
						}
					}
					htmlMessage.append("</ul>");
					htmlMessage.append("Thanks for all you do to help out, let me know if you have any questions or concerns.<br/><br/>Phillip<br/>214-470-5695</p></body></html>");

					HtmlEmail email = new HtmlEmail();
					email.setHostName(hostname);
					email.setSmtpPort(smtpPort);
					email.setAuthenticator(new DefaultAuthenticator(username, password));
					email.setSSL(true);
					email.setFrom(fromEmail, fromName);
					email.setSubject("Your updated home teaching assignment");
					email.setHtmlMsg(htmlMessage.toString());
					email.setTextMsg("This is a test mail ... :-)");
					email.addTo(user.getEmail(), toName);
					email.send();
				} catch (Exception e) {
					StringBuilder exception = new StringBuilder();
					exception.append("Exception sending email to ").append(user.getUsername()).append(":");
					logger.error(exception.toString(), e);
				}
			}
		}
		return true;
	}
}
