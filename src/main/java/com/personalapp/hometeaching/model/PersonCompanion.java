package com.personalapp.hometeaching.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "personcompanion")
public class PersonCompanion extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "personid")
	private Long personId;

	@Column(name = "companionid", insertable = false, updatable = false)
	private Long companionId;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "organizationid")
	private Long organizationId;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "personid", insertable = false, updatable = false)
	private Person person;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "companionid")
	private Companion companion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getCompanionId() {
		return companionId;
	}

	public void setCompanionId(Long companionId) {
		this.companionId = companionId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Organization getOrganization() {
		return Organization.fromId(organizationId);
	}

	public void setOrganization(Organization organization) {
		this.organizationId = organization.getId();
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Companion getCompanion() {
		return companion;
	}

	public void setCompanion(Companion companion) {
		this.companion = companion;
	}
}
