package com.personalapp.hometeaching.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "notes")
	private String notes;

	@Column(name = "userid")
	private Long userId;

	@Column(name = "resolved")
	private Boolean resolved;

	@Column(name = "priorityid")
	private Long priorityId;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	private HometeachingUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getResolved() {
		return resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public Priority getPriority() {
		return Priority.fromId(priorityId);
	}

	public void setPriority(Priority priority) {
		this.priorityId = priority.getId();
	}

	public HometeachingUser getUser() {
		return user;
	}
}
