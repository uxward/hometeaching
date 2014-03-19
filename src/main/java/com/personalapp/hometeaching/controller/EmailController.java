package com.personalapp.hometeaching.controller;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
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

	@Value("${email.elders.username}")
	private String username;

	@Value("${email.elders.password}")
	private String password;

	@Value("${email.elders.fromEmail}")
	private String fromEmail;

	@Value("${email.elders.fromName}")
	private String fromName;

	@Autowired
	private HometeachingUserService userService;
	@Autowired
	private CompanionService companionService;

	@RequestMapping()
	@ResponseBody()
	public Boolean sendEmail() {
		for (Companion companion : companionService.getAllCompanionsAndActiveFamilies()) {
			try {
				doSendEmail(companion, userService.getCompanionsToEmail(companion.getId()));
			} catch (Exception e) {
				StringBuilder exception = new StringBuilder();
				exception.append("Exception sending email to companion with id ").append(companion.getId()).append(":");
				logger.error(exception.toString(), e);
			}
		}

		return true;
	}

	@RequestMapping("byCompanion")
	@ResponseBody()
	public Boolean sendEmailForCompanion(@RequestParam Long companionId) {
		Companion companion = companionService.getCompanionAndActiveFamilies(companionId);
		try {
			doSendEmail(companion, userService.getCompanionsToEmail(companionId));
		} catch (Exception e) {
			StringBuilder exception = new StringBuilder();
			exception.append("Exception sending email to companion with id ").append(companion.getId()).append(":");
			logger.error(exception.toString(), e);
		}

		return true;
	}

	private void doSendEmail(Companion companion, List<HometeachingUser> users) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(hostname);
		email.setSmtpPort(smtpPort);
		email.setAuthenticator(new DefaultAuthenticator(username, password));
		email.setSSL(true);
		email.setFrom(fromEmail, fromName);
		email.setSubject("Updated home teaching assignment");
		email.setHtmlMsg(getHandlebars(companion));
		email.setTextMsg(getTextMessage(companion));

		for (HometeachingUser user : users) {
			if (isNotEmpty(user.getEmail())) {
				email.addTo(user.getEmail(), user.getPerson().getFullName());
			}
		}
		email.send();
	}

	private String getTextMessage(Companion companion) {

		StringBuilder htmlMessage = new StringBuilder();
		for (PersonCompanion personCompanion : companion.getCompanions()) {
			if (htmlMessage.length() != 0) {
				htmlMessage.append(" and ");
			}
			htmlMessage.append(personCompanion.getPerson().getFirstName());
		}
		htmlMessage
				.append(",\r\nHere are your most up to date home teaching assignments. To see more information about these families and record visits with them you can use the home teaching website at http://hometeaching.philman.cloudbees.net/companion/you\r\n");
		appendAssignment(companion, htmlMessage);
		htmlMessage.append("\r\nThanks for all you do to help out, and let me know if you have any questions or concerns.\r\n\r\nPhillip\r\n214-470-5695");
		return htmlMessage.toString();
	}

	private void appendAssignment(Companion companion, StringBuilder htmlMessage) {
		for (Assignment assignment : companion.getAssignments()) {
			if (assignment.getActive()) {
				htmlMessage.append("\r\n");
				String assignedFamily = null;
				for (Person person : assignment.getFamily().getHeadOfHousehold()) {
					if (assignedFamily == null) {
						assignedFamily = person.getFirstName();
					} else {
						assignedFamily += " and " + person.getFirstName();
					}
				}
				assignedFamily += " " + assignment.getFamily().getFamilyName();
				htmlMessage.append(assignedFamily);
				htmlMessage.append("\r\n");
			}
		}
	}

	private String getHandlebars(Companion companion) {
		StringWriter writer = new StringWriter();
		try {
			TemplateLoader loader = new ClassPathTemplateLoader("/templates", ".html");
			Handlebars handlebars = new Handlebars(loader);
			handlebars.registerHelper("stripeRows", new Helper<Integer>() {
				@Override
				public CharSequence apply(final Integer value, final Options options) throws IOException {
					return isEven(value + 1) ? "" : "background-color: #f9f9f9;";
				}

				protected boolean isEven(final Integer value) {
					return value % 2 == 0;
				}
			});
			handlebars.registerHelper("firstNoAnd", new Helper<Integer>() {
				@Override
				public CharSequence apply(final Integer value, final Options options) throws IOException {
					return value == 0 ? "" : " and ";
				}
			});
			handlebars.registerHelper("firstNoComma", new Helper<Integer>() {
				@Override
				public CharSequence apply(final Integer value, final Options options) throws IOException {
					return value == 0 ? "" : ", ";
				}
			});

			Template template = handlebars.compile("companionEmail.mustache");

			template.apply(companion, writer);
		} catch (IOException e) {
			logger.error("An unknown error occurred while trying to create the companion email template for companion with id " + companion.getId() + ": {}", e);
		}

		return writer.toString();
	}
}
