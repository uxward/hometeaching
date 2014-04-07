package com.personalapp.hometeaching.model;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "visit")
public class Visit extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "assignmentid")
	private Long assignmentId;

	@Column(name = "familyid")
	private Long familyId;

	@Column(name = "organizationid")
	private Long organizationId;

	@Column(name = "visited")
	private Boolean visited;

	@Column(name = "visited", insertable = false, updatable = false)
	private Integer intVisited;

	@Column(name = "notes")
	private String notes;

	@Column(name = "visitingteaching")
	private Boolean visitingTeaching;

	@Column(name = "month")
	private Integer month;

	@Column(name = "year")
	private Integer year;

	@Column(name = "visitdate")
	private Date visitDate;

	@ManyToOne
	@JoinColumn(name = "familyid", insertable = false, updatable = false)
	private Family family;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getVisited() {
		return visited;
	}

	public Integer getIntVisited() {
		return intVisited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getVisitingTeaching() {
		return visitingTeaching;
	}

	public void setVisitingTeaching(Boolean visitingTeaching) {
		this.visitingTeaching = visitingTeaching;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(visitDate);
		this.month = calendar.get(MONTH) + 1;
		this.year = calendar.get(YEAR);
		calendar.add(DAY_OF_MONTH, 1);
		this.visitDate = calendar.getTime();
	}
}
