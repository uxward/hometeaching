package com.personalapp.hometeaching.model;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.Organization.fromId;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "female")
	private Boolean female;

	@Column(name = "headofhousehold")
	private Boolean headOfHousehold;

	@Column(name = "familyid")
	private Long familyId;

	@Column(name = "phonenumber")
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "hometeacher")
	private Boolean hometeacher;

	@Column(name = "organizationid")
	private Long organizationId;

	@OneToMany(fetch = LAZY, mappedBy = "person")
	private List<PersonCompanion> personCompanion = newArrayList();

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "familyId", insertable = false, updatable = false)
	private Family family;

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

	public Boolean getFemale() {
		return female;
	}

	public void setFemale(Boolean female) {
		this.female = female;
	}

	public Boolean getHeadOfHousehold() {
		return headOfHousehold;
	}

	public void setHeadOfHousehold(Boolean headOfHousehold) {
		this.headOfHousehold = headOfHousehold;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
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

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public void setOrganization(Organization organization) {
		this.organizationId = organization.getId();
	}

	public Organization getOrganization() {
		return fromId(organizationId);
	}

	public List<PersonCompanion> getPersonCompanion() {
		return personCompanion;
	}

	public void setPersonCompanion(List<PersonCompanion> personCompanion) {
		this.personCompanion = personCompanion;
	}

	public PersonCompanion getActiveCompanion() {
		PersonCompanion active = null;
		for (PersonCompanion personCompanion : this.personCompanion) {
			if (personCompanion.getActive()) {
				active = personCompanion;
				break;
			}
		}
		return active;
	}

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}
}
