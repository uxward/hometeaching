package com.personalapp.hometeaching.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
	public void setupCreateInformation() {
		this.created = new Date();
	}

	@PreUpdate
	public void setupUpdateInformation() {
		this.updated = new Date();
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
