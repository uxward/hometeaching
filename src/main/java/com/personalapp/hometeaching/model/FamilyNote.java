package com.personalapp.hometeaching.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
