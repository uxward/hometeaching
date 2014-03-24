package com.personalapp.hometeaching.io.messaging;

import javax.mail.Authenticator;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.personalapp.hometeaching.model.Organization;

@Component
public class EmailConfig {

	@Value("${email.hostname}")
	private String hostname;

	@Value("${email.smtpPort}")
	private Integer smtpPort;

	/*
	 * Elders configuration
	 */

	@Value("${email.elders.username}")
	private String eldersUsername;

	@Value("${email.elders.password}")
	private String eldersPassword;

	@Value("${email.elders.fromEmail}")
	private String eldersFromEmail;

	@Value("${email.elders.fromName}")
	private String eldersFromName;

	@Value("${email.elders.fromNumber}")
	private String eldersFromNumber;

	/*
	 * High priests configuration
	 */

	@Value("${email.highpriests.username}")
	private String highPriestsUsername;

	@Value("${email.highpriests.password}")
	private String highPriestsPassword;

	@Value("${email.highpriests.fromEmail}")
	private String highPriestsFromEmail;

	@Value("${email.highpriests.fromName}")
	private String highPriestsFromName;

	@Value("${email.highpriests.fromNumber}")
	private String highPriestsFromNumber;

	/*
	 * Relief society configuration
	 */

	@Value("${email.reliefsociety.username}")
	private String reliefSocietyUsername;

	@Value("${email.reliefsociety.password}")
	private String reliefSocietyPassword;

	@Value("${email.reliefsociety.fromEmail}")
	private String reliefSocietyFromEmail;

	@Value("${email.reliefsociety.fromName}")
	private String reliefSocietyFromName;

	@Value("${email.reliefsociety.fromNumber}")
	private String reliefSocietyFromNumber;

	public HtmlEmail getEmailForOrganization(Organization organization) throws EmailException {
		return setupEmailForOrganization(organization);
	}

	public BaseEmailTemplate getTemplateForOrganization(Organization organization) {
		BaseEmailTemplate template = new BaseEmailTemplate();
		template.setFromName(getFromNameByOrganization(organization));
		template.setFromNumber(getFromNumberByOrganization(organization));
		return template;
	}

	private HtmlEmail setupEmailForOrganization(Organization organization) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(getHostNameByOrganization(organization));
		email.setSmtpPort(getSmtpPortByOrganization(organization));
		email.setAuthenticator(getAuthenticationByOrganization(organization));
		email.setSSL(true);
		email.setFrom(getFromEmailByOrganization(organization), getFromNameByOrganization(organization));
		return email;
	}

	private String getFromEmailByOrganization(Organization organization) {
		String fromEmail = null;
		switch (organization) {
		case ELDERS:
			fromEmail = eldersFromEmail;
			break;
		case HIGH_PRIEST:
			fromEmail = highPriestsFromEmail;
			break;
		case RELIEF_SOCIETY:
			fromEmail = reliefSocietyFromEmail;
			break;
		case WARD:
			break;
		default:
			break;
		}
		return fromEmail;
	}

	private String getFromNameByOrganization(Organization organization) {
		String fromName = null;
		switch (organization) {
		case ELDERS:
			fromName = eldersFromName;
			break;
		case HIGH_PRIEST:
			fromName = highPriestsFromName;
			break;
		case RELIEF_SOCIETY:
			fromName = reliefSocietyFromName;
			break;
		case WARD:
			break;
		default:
			break;
		}
		return fromName;
	}

	private String getFromNumberByOrganization(Organization organization) {
		String fromNumber = null;
		switch (organization) {
		case ELDERS:
			fromNumber = eldersFromNumber;
			break;
		case HIGH_PRIEST:
			fromNumber = highPriestsFromNumber;
			break;
		case RELIEF_SOCIETY:
			fromNumber = reliefSocietyFromNumber;
			break;
		case WARD:
			break;
		default:
			break;
		}
		return fromNumber;
	}

	private Authenticator getAuthenticationByOrganization(Organization organization) {
		Authenticator authenticator = null;
		switch (organization) {
		case ELDERS:
			authenticator = new DefaultAuthenticator(eldersUsername, eldersPassword);
			break;
		case HIGH_PRIEST:
			authenticator = new DefaultAuthenticator(highPriestsUsername, highPriestsPassword);
			break;
		case RELIEF_SOCIETY:
			authenticator = new DefaultAuthenticator(reliefSocietyUsername, reliefSocietyPassword);
			break;
		case WARD:
			break;
		default:
			break;
		}
		return authenticator;
	}

	private int getSmtpPortByOrganization(Organization organization) {
		return smtpPort;
	}

	private String getHostNameByOrganization(Organization organization) {
		return hostname;
	}
}
