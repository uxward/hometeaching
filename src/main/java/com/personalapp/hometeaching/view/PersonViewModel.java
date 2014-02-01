package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.model.PersonCompanion;

public class PersonViewModel {

	private Long id;

	private String firstName;

	private Integer age;

	private Boolean female;

	private Boolean headOfHousehold;

	private String phoneNumber;

	private String email;

	private Boolean hometeacher;

	private OrganizationViewModel organization;

	private FamilyViewModel family;

	private Boolean activeCompanion;

	public PersonViewModel(Person person, Boolean populateFamily,
			Boolean populateCompanion) {
		this.id = person.getId();
		this.firstName = person.getFirstName();
		this.age = person.getAge();
		this.email = person.getEmail();
		this.female = person.getFemale();
		this.headOfHousehold = person.getHeadOfHousehold();
		this.phoneNumber = person.getPhoneNumber();
		this.hometeacher = person.getHometeacher();

		if (person.getOrganization() != null) {
			this.organization = new OrganizationViewModel(
					person.getOrganization());
		}

		if (populateFamily) {
			this.family = new FamilyViewModel(person.getFamily(), false, false);
		}

		if (populateCompanion) {
			for (PersonCompanion personCompanion : person.getPersonCompanion()) {
				if (personCompanion.getActive()) {
					setActiveCompanion(true);
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

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getFemale() {
		return female;
	}

	public void setFemale(Boolean female) {
		this.female = female;
	}

	public Boolean getHeadOfHousehold() {
		return headOfHousehold;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getHometeacher() {
		return hometeacher;
	}

	public void setHometeacher(Boolean hometeacher) {
		this.hometeacher = hometeacher;
	}

	public OrganizationViewModel getOrganization() {
		return organization;
	}

	public FamilyViewModel getFamily() {
		return family;
	}

	public void setFamily(FamilyViewModel family) {
		this.family = family;
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

	public void setActiveCompanion(Boolean activeCompanion) {
		this.activeCompanion = activeCompanion;
	}

}
