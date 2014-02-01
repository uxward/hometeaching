package com.personalapp.hometeaching.model;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "family")
public class Family extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "familyname")
	private String familyName;

	@Column(name = "address")
	private String address;

	@Column(name = "phonenumber")
	private String phoneNumber;

	@Column(name = "familystatusid")
	private Long familyStatusId;

	@Transient
	private List<Long> familyOrganizationIds = newArrayList();

	@OneToMany(fetch = LAZY, mappedBy = "family", cascade = ALL, orphanRemoval = true)
	private Set<FamilyOrganization> familyOrganizations = newHashSet();

	@OneToMany(fetch = LAZY, mappedBy = "family")
	private Set<Person> people = newHashSet();

	@OneToMany(fetch = LAZY, mappedBy = "family")
	private List<Assignment> assignment = newArrayList();

	@OneToMany(fetch = LAZY, mappedBy = "family")
	private Set<Visit> visits = newHashSet();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<Person> getPeople() {
		return people;
	}

	public void setPeople(Set<Person> people) {
		this.people = people;
	}

	public List<Assignment> getAssignment() {
		return assignment;
	}

	public void setAssignment(List<Assignment> assignment) {
		this.assignment = assignment;
	}

	public void setFamilyStatus(FamilyStatus status) {
		this.familyStatusId = status.getId();
	}

	public void setFamilyStatusId(Long familyStatusId) {
		this.familyStatusId = familyStatusId;
	}

	public FamilyStatus getFamilyStatus() {
		return FamilyStatus.fromId(familyStatusId);
	}

	public String getFamilyStatusString() {
		return FamilyStatus.fromId(familyStatusId).getStatus();
	}

	public Set<FamilyOrganization> getFamilyOrganizations() {
		return familyOrganizations;
	}

	public void setFamilyOrganizations(
			Set<FamilyOrganization> familyOrganizations) {
		this.familyOrganizations = familyOrganizations;
	}

	public List<Long> getFamilyOrganizationIds() {
		return familyOrganizationIds;
	}

	public void setFamilyOrganizationIds(List<Long> familyOrganizationIds) {
		this.familyOrganizationIds = familyOrganizationIds;
	}

	public Assignment getActiveAssignment() {
		Assignment active = new Assignment();
		for (Assignment assignment : this.assignment) {
			if (assignment.getActive()) {
				active = assignment;
				break;
			}
		}
		return active;
	}

	public Set<Visit> getVisits() {
		return visits;
	}

	public Set<Person> getHeadOfHousehold() {
		Set<Person> headOfHousehold = newHashSet();
		for (Person person : people) {
			if (person.getHeadOfHousehold()) {
				headOfHousehold.add(person);
			}
		}
		return headOfHousehold;
	}
}
