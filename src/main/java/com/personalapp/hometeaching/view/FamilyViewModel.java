package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyOrganization;
import com.personalapp.hometeaching.model.Person;

public class FamilyViewModel {
	private Long id;

	private Long assignmentId;

	private String familyName;

	private String address;

	private String phoneNumber;

	private String familyStatus;

	private List<OrganizationViewModel> organizations = newArrayList();

	private List<PhoneViewModel> phoneNumbers = newArrayList();

	private List<PersonViewModel> people = newArrayList();

	private CompanionViewModel companions;

	public FamilyViewModel(Family family, Boolean populatePeople, Boolean populateHometeachers) {
		this.id = family.getId();
		this.familyName = family.getFamilyName();
		this.address = family.getAddress();
		this.phoneNumber = family.getPhoneNumber();
		if (family.getFamilyStatus() != null) {
			this.familyStatus = family.getFamilyStatus().getStatus();
		}

		for (FamilyOrganization organization : family.getFamilyOrganizations()) {
			this.organizations.add(new OrganizationViewModel(organization.getOrganization()));
		}

		if (family.getPhoneNumber() != null && family.getPhoneNumber().trim().length() > 0) {
			this.phoneNumbers.add(new PhoneViewModel(family));
		}

		if (populatePeople) {
			for (Person person : family.getPeople()) {
				this.people.add(new PersonViewModel(person, false, false));
				if (person.getPhoneNumber() != null && person.getPhoneNumber().trim().length() > 0) {
					this.phoneNumbers.add(new PhoneViewModel(person));
				}
			}
		}

		if (populateHometeachers) {
			this.companions = new CompanionViewModel(family.getActiveAssignment().getCompanion(), false);
		}
	}

	public FamilyViewModel() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFamilyStatus() {
		return familyStatus;
	}

	public List<OrganizationViewModel> getOrganizations() {
		return organizations;
	}

	public List<PhoneViewModel> getPhoneNumbers() {
		return phoneNumbers;
	}

	public List<PersonViewModel> getPeople() {
		return people;
	}

	public void setPeople(List<PersonViewModel> people) {
		this.people = people;
	}

	public String getPeopleNames() {
		String names = "";
		for (PersonViewModel person : people) {
			if (!names.equals("")) {
				names += ", ";
			}
			names += person.getFirstName();
		}
		return names;
	}

	public String getHeadOfHousehold() {
		String names = "";
		for (PersonViewModel person : people) {
			if (person.getHeadOfHousehold()) {
				if (!names.equals("")) {
					names += " and ";
				}
				names += person.getFirstName();
			}
		}
		return names;
	}

	public CompanionViewModel getCompanions() {
		return companions;
	}

	public void setCompanions(CompanionViewModel hometeachers) {
		this.companions = hometeachers;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}
}
