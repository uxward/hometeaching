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
@Table(name = "userorganization")
public class UserOrganization {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "userid", insertable = false, updatable = false)
	private Long userId;

	@Column(name = "organizationid")
	private Long organizationId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "userId")
	private HometeachingUser hometeachingUser;

	public Long getId() {
		return id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
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

	public void setHometeachingUser(HometeachingUser hometeachingUser) {
		this.hometeachingUser = hometeachingUser;
	}

	public HometeachingUser getHometeachingUser() {
		return hometeachingUser;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof UserOrganization)) {
			return false;
		}
		UserOrganization other = (UserOrganization) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getUserId(), other.getUserId()).append(getOrganizationId(), other.getOrganizationId());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getUserId()).append(getOrganizationId());
		return hashCodeBuilder.toHashCode();
	}
}
