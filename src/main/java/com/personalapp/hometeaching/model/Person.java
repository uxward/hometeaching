package com.personalapp.hometeaching.model;

import static com.google.common.collect.Lists.newArrayList;
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
	private Boolean homeTeacher;

	@Column(name = "visitingteacher")
	private Boolean visitingTeacher;

	@Column(name = "user")
	private Boolean user;

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

	public String getFullName() {
		if (this.family == null) {
			return firstName;
		} else {
			return firstName + " " + family.getFamilyName();
		}
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

	public Boolean getHomeTeacher() {
		return homeTeacher;
	}

	public void setHomeTeacher(Boolean hometeacher) {
		this.homeTeacher = hometeacher;
	}

	public Boolean getVisitingTeacher() {
		return visitingTeacher;
	}

	public void setVisitingTeacher(Boolean visitingTeacher) {
		this.visitingTeacher = visitingTeacher;
	}

	public Boolean getUser() {
		return user;
	}

	public void setUser(Boolean user) {
		this.user = user;
	}

	public List<PersonCompanion> getPersonCompanion() {
		return personCompanion;
	}

	public void setPersonCompanion(List<PersonCompanion> personCompanion) {
		this.personCompanion = personCompanion;
	}

	public List<Companion> getActiveCompanions(boolean visitingTeaching) {
		List<Companion> active = newArrayList();
		for (PersonCompanion personCompanion : this.personCompanion) {
			Companion companion = personCompanion.getCompanion();
			if (visitingTeaching) {
				if (companion.getActive() && companion.isVisitingTeaching()) {
					active.add(companion);
				}
			} else {
				if (companion.getActive() && !companion.isVisitingTeaching()) {
					active.add(companion);
				}
			}
		}
		return active;
	}

	public List<Companion> getActiveCompanions() {
		List<Companion> active = newArrayList();
		for (PersonCompanion personCompanion : this.personCompanion) {
			Companion companion = personCompanion.getCompanion();
			if (companion.getActive()) {
				active.add(companion);
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
