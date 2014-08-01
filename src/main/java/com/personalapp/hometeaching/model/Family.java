package com.personalapp.hometeaching.model;

import static com.google.common.collect.Lists.newArrayList;
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

@Entity
@Table(name = "family")
public class Family extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "familyname")
	private String familyName;

	@Column(name = "address")
	private String address;

	@Column(name = "phonenumber")
	private String phoneNumber;

	@Column(name = "familyMoved")
	private Boolean familyMoved;

	@Column(name = "recordsMoved")
	private Boolean recordsMoved;

	@Column(name = "partmember")
	private Boolean partMember;

	@Column(name = "familystatusid")
	private Long familyStatusId;

	@Transient
	private List<Long> familyOrganizationIds = newArrayList();

	@OneToMany(fetch = LAZY, mappedBy = "family", cascade = ALL, orphanRemoval = true)
	private Set<FamilyOrganization> familyOrganizations = newHashSet();

	@OneToMany(fetch = LAZY, mappedBy = "family")
	private Set<Person> people = newHashSet();

	@OneToMany(fetch = LAZY, mappedBy = "family")
	private List<Assignment> assignments = newArrayList();

	@OneToMany(fetch = LAZY, mappedBy = "family")
	private Set<Visit> visits = newHashSet();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getFamilyMoved() {
		return familyMoved;
	}

	public void setFamilyMoved(Boolean familyMoved) {
		this.familyMoved = familyMoved;
	}

	public Boolean getRecordsMoved() {
		return recordsMoved;
	}

	public void setRecordsMoved(Boolean recordsMoved) {
		this.recordsMoved = recordsMoved;
	}

	public Boolean getPartMember() {
		return partMember;
	}

	public void setPartMember(Boolean partMember) {
		this.partMember = partMember;
	}

	public Set<Person> getPeople() {
		return people;
	}

	public void setPeople(Set<Person> people) {
		this.people = people;
	}

	public List<Assignment> getAssignment() {
		return assignments;
	}

	public void setAssignment(List<Assignment> assignment) {
		this.assignments = assignment;
	}

	public void setFamilyStatus(FamilyStatus status) {
		this.familyStatusId = status.getId();
	}

	public void setFamilyStatusId(Long familyStatusId) {
		this.familyStatusId = familyStatusId;
	}

	public FamilyStatus getFamilyStatus() {
		return FamilyStatus.fromId(familyStatusId);
	}

	public String getFamilyStatusString() {
		return FamilyStatus.fromId(familyStatusId).getStatus();
	}

	public Set<FamilyOrganization> getFamilyOrganizations() {
		return familyOrganizations;
	}

	public void setFamilyOrganizations(
			Set<FamilyOrganization> familyOrganizations) {
		this.familyOrganizations = familyOrganizations;
	}

	public List<Long> getFamilyOrganizationIds() {
		return familyOrganizationIds;
	}

	public void setFamilyOrganizationIds(List<Long> familyOrganizationIds) {
		this.familyOrganizationIds = familyOrganizationIds;
	}

	public Companion getActiveCompanion(boolean visitingTeaching) {
		Companion active = new Companion();
		Assignment assignment = getActiveAssignment(visitingTeaching);
		if (assignment.getCompanion() != null) {
			active = assignment.getCompanion();
		}
		return active;
	}

	public Assignment getActiveAssignment(boolean visitingTeaching) {
		Assignment assignment = new Assignment();
		for (Assignment ass : this.assignments) {
			if (ass.getActive()) {
				Companion companion = assignment.getCompanion();
				if (companion.isVisitingTeaching() == visitingTeaching) {
					assignment = ass;
					break;
				}
			}
		}
		return assignment;
	}

	public Set<Visit> getHomeTeachingVisits() {
		Set<Visit> homeTeachingVisits = newHashSet();
		for (Visit visit : visits) {
			if (!visit.getVisitingTeaching()) {
				homeTeachingVisits.add(visit);
			}
		}
		return homeTeachingVisits;
	}

	public Set<Visit> getVisitingTeachingVisits() {
		Set<Visit> visitingTeachingVisits = newHashSet();
		for (Visit visit : visits) {
			if (visit.getVisitingTeaching()) {
				visitingTeachingVisits.add(visit);
			}
		}
		return visitingTeachingVisits;
	}

	public Set<Person> getHeadOfHousehold() {
		Set<Person> headOfHousehold = newHashSet();
		for (Person person : people) {
			if (person.getHeadOfHousehold()) {
				headOfHousehold.add(person);
			}
		}
		return headOfHousehold;
	}

	public Set<Person> getWomenHeadOfHousehold() {
		return getHeadOfHousehold(true);
	}

	public Set<Person> getMenHeadOfHousehold() {
		return getHeadOfHousehold(false);
	}

	public Set<Person> getHeadOfHousehold(boolean female) {
		Set<Person> headOfHousehold = newHashSet();
		for (Person person : people) {
			if (person.getHeadOfHousehold() && person.getFemale() == female) {
				headOfHousehold.add(person);
			}
		}
		return headOfHousehold;
	}

	public Set<Person> getChildren() {
		Set<Person> children = newHashSet();
		for (Person person : people) {
			if (!person.getHeadOfHousehold()) {
				children.add(person);
			}
		}
		return children;
	}
}
