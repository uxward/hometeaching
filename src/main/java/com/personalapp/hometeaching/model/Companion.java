package com.personalapp.hometeaching.model;

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

import org.springframework.util.AutoPopulatingList;

@Entity
@Table(name = "companion")
public class Companion extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "organizationid")
	private Long organizationId;

	@OneToMany(fetch = LAZY, mappedBy = "companion", cascade = ALL)
	private Set<PersonCompanion> companions = newHashSet();

	@OneToMany(fetch = LAZY, mappedBy = "companion", cascade = ALL)
	private Set<Assignment> assignments = newHashSet();

	@Transient
	private List<PersonCompanion> autopopulatingPersonCompanions = new AutoPopulatingList<PersonCompanion>(PersonCompanion.class);

	@Transient
	private List<Assignment> autopopulatingAssignments = new AutoPopulatingList<Assignment>(Assignment.class);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Set<PersonCompanion> getCompanions() {
		return companions;
	}

	public void setCompanions(Set<PersonCompanion> companions) {
		this.companions = companions;
	}

	public Set<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(Set<Assignment> assignments) {
		this.assignments = assignments;
	}

	public List<PersonCompanion> getAutopopulatingPersonCompanions() {
		return autopopulatingPersonCompanions;
	}

	public void setAutopopulatingPersonCompanions(List<PersonCompanion> autopopulatingPersonCompanions) {
		this.autopopulatingPersonCompanions = autopopulatingPersonCompanions;
	}

	public List<Assignment> getAutopopulatingAssignments() {
		return autopopulatingAssignments;
	}

	public void setAutopopulatingAssignments(List<Assignment> autopopulatingAssignments) {
		this.autopopulatingAssignments = autopopulatingAssignments;
	}

	public boolean isVisitingTeaching() {
		return Organization.isVisitingTeaching(getOrganization());
	}
}
