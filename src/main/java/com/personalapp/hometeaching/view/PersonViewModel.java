package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.model.PersonCompanion;

public class PersonViewModel {

	private Long id;

	private String firstName;

	private Boolean female;

	private Boolean headOfHousehold;

	private String phoneNumber;

	private String email;

	private Boolean hometeacher;

	private Boolean user;

	private OrganizationViewModel organization;

	private FamilyViewModel family;

	private Boolean activeCompanion;

	public PersonViewModel(Person person, Boolean populateFamily, Boolean populateCompanion) {
		this.id = person.getId();
		this.firstName = person.getFirstName();
		this.email = person.getEmail();
		this.female = person.getFemale();
		this.headOfHousehold = person.getHeadOfHousehold();
		this.phoneNumber = person.getPhoneNumber();
		this.hometeacher = person.getHometeacher();
		this.user = person.getUser();

		if (person.getOrganization() != null) {
			this.organization = new OrganizationViewModel(person.getOrganization(), null);
		}

		if (populateFamily) {
			this.family = new FamilyViewModel(person.getFamily(), false, false, false);
		}

		if (populateCompanion) {
			for (PersonCompanion personCompanion : person.getPersonCompanion()) {
				if (personCompanion.getActive()) {
					this.activeCompanion = true;
					break;
				}
			}
		}
	}

	public PersonViewModel() {

	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public Boolean getFemale() {
		return female;
	}

	public Boolean getHeadOfHousehold() {
		return headOfHousehold;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getHometeacher() {
		return hometeacher;
	}
	
	public Boolean getUser(){
		return user;
	}

	public OrganizationViewModel getOrganization() {
		return organization;
	}

	public FamilyViewModel getFamily() {
		return family;
	}

	public String getFullName() {
		if (family != null) {
			return firstName + " " + family.getFamilyName();
		} else {
			return firstName;
		}
	}

	public Boolean getActiveCompanion() {
		return activeCompanion;
	}
}
