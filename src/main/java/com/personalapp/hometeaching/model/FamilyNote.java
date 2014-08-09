package com.personalapp.hometeaching.model;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.personalapp.hometeaching.security.SecurityUtils;

@Entity
@Table(name = "familynote")
public class FamilyNote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "familyid")
	private Long familyId;

	@Column(name = "note")
	private String note;

	@Column(name = "visibleRole")
	private String visibleRole;

	// TODO add these columns to baseentity
	// @Column(name = "createdby")
	// private Long createdBy;
	//
	// @PrePersist
	// public void setupCreateInformation() {
	// this.createdBy = getCurrentUser().getId();
	// }

	// TODO add these columns to baseentity
	// @Column(name = "updatedby")
	// private Long updatedBy;
	//
	// @PreUpdate
	// public void setupUpdateInformation() {
	// this.updatedBy = getCurrentUser().getId();
	// }

	@ManyToOne
	@JoinColumn(name = "familyid", insertable = false, updatable = false)
	private Family family;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getVisibleRole() {
		return visibleRole;
	}

	public void setVisibleRole(String visibleRole) {
		this.visibleRole = visibleRole;
	}

	public Family getFamily() {
		return family;
	}
}
