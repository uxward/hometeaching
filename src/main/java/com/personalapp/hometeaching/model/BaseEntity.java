package com.personalapp.hometeaching.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

@MappedSuperclass
public class BaseEntity {

	@Column(name = "created")
	private Date created;

	@Column(name = "updated")
	private Date updated;

	// TODO add these columns to db tables
	// @Column(name = "createdby")
	// private Long createdBy;
	//
	// TODO add these columns to db tables
	// @Column(name = "updatedby")
	// private Long updatedBy;

	@PrePersist
	public void setupUserAndDateInformation() {
		if (this.created == null) {
			this.created = new Date();
		} else {
			this.updated = new Date();
		}
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
