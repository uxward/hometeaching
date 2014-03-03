package com.personalapp.hometeaching.controller;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
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

		for (Companion companion : companionService.getAllCompanionsAndActiveFamilies()) {
			try {
				List<HometeachingUser> users = userService.getCompanionsToEmail(companion.getId());

				doSendEmail(companion, users);
			} catch (Exception e) {
				StringBuilder exception = new StringBuilder();
				exception.append("Exception sending email to companion with id ").append(companion.getId()).append(":");
				logger.error(exception.toString(), e);
			}
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
		email.setHtmlMsg(getMustache(companion));
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
		htmlMessage.append(",\r\nHere are your most up to date home teaching assignments:\r\n");
		for (Assignment assignment : companion.getAssignments()) {
			if (assignment.getActive()) {
				htmlMessage.append("\r\n");
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
				htmlMessage.append("\r\n");
			}
		}
		htmlMessage
				.append("\r\nTo see more information about these families and record visits with them you can use the home teaching website at http://hometeaching.philman.cloudbees.net/companion/you.  Thanks for all you do to help out, and let me know if you have any questions or concerns.\r\n\r\nPhillip\r\n214-470-5695");
		return htmlMessage.toString();
	}

	private String getHtmlMessage(Companion companion) {
		StringBuilder htmlMessage = new StringBuilder();
		for (PersonCompanion personCompanion : companion.getCompanions()) {
			if (htmlMessage.length() != 0) {
				htmlMessage.append(" and ");
			}
			htmlMessage.append(personCompanion.getPerson().getFirstName());
		}
		htmlMessage.append(",<br/>Here are your most up to date home teaching assignments:<br/>");
		for (Assignment assignment : companion.getAssignments()) {
			if (assignment.getActive()) {
				htmlMessage.append("<br/>");
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
			}
		}
		htmlMessage
				.append("<br/><br/>To see more information about these families and record visits with them you can use the <a href=\"http://hometeaching.philman.cloudbees.net/companion/you\">home teaching website</a>.  Thanks for all you do to help out, and let me know if you have any questions or concerns.<br/><br/>Phillip<br/>214-470-5695</p></body></html>");

		htmlMessage = new StringBuilder();
		htmlMessage
				.append("</p><table class=\"table table-striped table-hover table-bordered dataTable\"> <thead> <tr> <throwspan=\"1\" colspan=\"1\"> Family </th> <th rowspan=\"1\" colspan=\"1\"> Status </th> <th rowspan=\"1\" colspan=\"1\"> Children </th> <th rowspan=\"1\" colspan=\"1\"> Address </th> <th rowspan=\"1\" colspan=\"1\"> Phone Numbers </th> </tr> </thead> <tbody> <tr class=\"odd\"> <td> Admin, wife and husband </td> <td> Active </td> <td> guest </td> <td> <a href=\"http://maps.google.com/maps?daddr=14501 Montfort Dr Apt 1511\">14501 Montfort Dr Apt 1511</a> </td> <td> wife: <a href=\"tel:801-358-7280\">801-358-7280</a>, husband: <a href=\"tel:214-470-5695\">214-470-5695</a> </td> </tr> <tr class=\"even\"> <td> Do not contact, man </td> <td> Do Not Contact </td> <td> </td> <td> <a href=\"http://maps.google.com/maps?daddr=\"></a> </td> <td> </td> </tr> </tbody> </table></body></html>");
		htmlMessage
				.insert(0,
						"<html><head><style>table { border: 1px solid #ddd; background-color: transparent; border-collapse: collapse; border-spacing: 0; display: table; font-family: \"Helvetica Neue\",Helvetica,Arial,sans-serif; font-size: 14px; line-height: 1.428571429; color: #333; }  thead{ display: table-header-group; vertical-align: middle; border-color: inherit; border-spacing: 2px; border-color: gray; }  tr{ display: table-row; vertical-align: inherit; border-color: inherit; }  th{ border-bottom-width: 2px; border: 1px solid #ddd; vertical-align: bottom; border-bottom: 2px solid #ddd; padding: 8px; line-height: 1.428571429; text-align: left; font-weight: bold; display: table-cell; }  tbody{ display: table-row-group; vertical-align: middle; border-color: inherit; } tr:nth-child(odd)>td{ background-color: #f9f9f9; }  td{ display: table-cell; padding: 8px; line-height: 1.428571429; vertical-align: top; border-top: 1px solid #ddd; }</style></head><body><p>");
		return htmlMessage.toString();
	}

	private String getMustache(Companion companion) {
		StringWriter writer = new StringWriter();
		try {
			File templatePath = ResourceUtils.getFile("classpath:templates/");
			MustacheFactory mf = new DefaultMustacheFactory(templatePath);
			Mustache mustache;

			mustache = mf.compile("companionEmail.mustache.html");

			mustache.execute(writer, companion).flush();
		} catch (IOException e) {
			logger.error("An unknown error occurred while trying to create the companion email template for companion with id " + companion.getId() + ": {}", e);
		}

		return writer.toString();
	}

}
