package com.personalapp.hometeaching.model;

import static com.google.common.collect.Sets.newHashSet;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "companion")
public class Companion extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "active")
	private Boolean active;

	@OneToMany(fetch = LAZY, mappedBy = "companion", cascade = ALL)
	private Set<PersonCompanion> companions = newHashSet();

	@OneToMany(fetch = LAZY, mappedBy = "companion", cascade = ALL)
	private Set<Assignment> assignments = newHashSet();

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
}
