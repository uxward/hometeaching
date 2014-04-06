package com.personalapp.hometeaching.model;

import static com.personalapp.hometeaching.model.Organization.RELIEF_SOCIETY;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "assignment")
public class Assignment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "companionId")
	private Long companionId;

	@Column(name = "familyId")
	private Long familyId;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "organizationid")
	private Long organizationId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "familyId", insertable = false, updatable = false)
	private Family family;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "companionId", insertable = false, updatable = false)
	private Companion companion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	public Companion getCompanion() {
		return companion;
	}

	public void setCompanion(Companion companion) {
		this.companion = companion;
	}

	public boolean isVisitingTeaching() {
		if (RELIEF_SOCIETY.equals(getOrganization())) {
			return true;
		}
		return false;
	}
}
