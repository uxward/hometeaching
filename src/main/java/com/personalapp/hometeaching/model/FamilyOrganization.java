package com.personalapp.hometeaching.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "familyorganization")
public class FamilyOrganization {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "familyid", insertable = false, updatable = false)
	private Long familyId;

	@Column(name = "organizationid")
	private Long organizationId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "familyid")
	private Family family;

	public Long getId() {
		return id;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganization(Organization organization) {
		this.organizationId = organization.getId();
	}

	public Organization getOrganization() {
		return Organization.fromId(organizationId);
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	public Family getFamily() {
		return family;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof FamilyOrganization)) {
			return false;
		}
		FamilyOrganization other = (FamilyOrganization) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getFamilyId(), other.getFamilyId()).append(
				getOrganizationId(), other.getOrganizationId());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getFamilyId()).append(getOrganizationId());
		return hashCodeBuilder.toHashCode();
	}
}
