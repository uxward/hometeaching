package com.personalapp.hometeaching.io.messaging;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.model.PersonCompanion;

@Component
public class EmailClient {
	private final Logger logger = getLogger(getClass());

	private Map<Organization, HtmlEmail> emails = newHashMap();
	private Handlebars handlebars;

	@Autowired
	private EmailConfig emailConfig;

	@PostConstruct
	private void setupEmails() throws EmailException {
		for (Organization organization : Organization.forDisplay()) {
			emails.put(organization, emailConfig.getEmailForOrganization(organization));
		}
		handlebars = setupHandlebars();
	}

	public void sendNewUserEmail(HometeachingUser user) throws EmailException, IOException {
		HtmlEmail email = setupNewUserEmail(user);
		email.send();
	}

	public void sendUpdatedAssignmentEmail(Companion companion, List<HometeachingUser> users) throws EmailException, IOException {
		HtmlEmail email = setupUpdatedAssignmentEmail(companion, users);
		email.send();
	}

	private HtmlEmail setupNewUserEmail(HometeachingUser user) throws EmailException, IOException {
		Organization organization = getUserOrganization(user);
		HtmlEmail email = emails.get(organization);
		email.setSubject("Home teaching website invite");
		UserEmailTemplate userEmailTemplate = new UserEmailTemplate(user);
		email.setHtmlMsg(getHtml("newUser.mustache", userEmailTemplate));
		addUserToEmail(email, user);
		return email;
	}

	private HtmlEmail setupUpdatedAssignmentEmail(Companion companion, List<HometeachingUser> users) throws IOException, EmailException {
		Organization organization = getCompanionOrganization(companion);
		HtmlEmail email = emails.get(organization);
		email.setSubject("Updated home teaching assignment");
		AssignmentEmailTemplate assignmentEmailTemplate = new AssignmentEmailTemplate(companion);
		email.setHtmlMsg(getHtml("updatedAssignment.mustache", assignmentEmailTemplate));
		for (HometeachingUser user : users) {
			addUserToEmail(email, user);
		}
		return email;
	}

	private void addUserToEmail(HtmlEmail email, HometeachingUser user) throws EmailException {
		if (isNotEmpty(user.getEmail())) {
			email.addTo(user.getEmail(), user.getPerson().getFullName());
		} else if (isNotEmpty(user.getPerson().getEmail())) {
			email.addTo(user.getEmail(), user.getPerson().getFullName());
		}
	}

	private Organization getCompanionOrganization(Companion companion) {
		Organization organization = null;
		for (PersonCompanion personCompanion : companion.getCompanions()) {
			if (personCompanion.getPerson() != null) {
				organization = personCompanion.getPerson().getOrganization();
			}
			if (organization != null) {
				break;
			}
		}
		return organization;
	}

	private Organization getUserOrganization(HometeachingUser user) {
		Organization organization = null;
		if (user.getPerson() != null) {
			organization = user.getPerson().getOrganization();
		}
		return organization;
	}

	private String getHtml(String templateFile, BaseEmailTemplate emailTemplate) throws IOException {
		StringWriter writer = new StringWriter();

		Template template = handlebars.compile(templateFile);
		template.apply(emailTemplate, writer);

		return writer.toString();
	}

	private Handlebars setupHandlebars() {
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
		return handlebars;
	}
}