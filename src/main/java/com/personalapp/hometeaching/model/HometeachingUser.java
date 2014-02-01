package com.personalapp.hometeaching.model;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

@Entity
@Table(name = "users")
public class HometeachingUser {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "personid")
	private Long personId;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "reset")
	private Boolean reset;

	@Column(name = "lastlogin")
	private Date lastLogin;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "personid", insertable = false, updatable = false)
	private Person person;

	@OneToMany(fetch = LAZY, mappedBy = "hometeachingUser", cascade = ALL, orphanRemoval = true)
	private Set<UserRole> userRoles = newHashSet();

	@Transient
	private List<String> userRoleIds = newArrayList();

	@Transient
	private List<Long> userOrganizationIds = newArrayList();

	@OneToMany(fetch = LAZY, mappedBy = "hometeachingUser", cascade = ALL, orphanRemoval = true)
	private Set<UserOrganization> userOrganizations = newHashSet();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getReset() {
		return reset;
	}

	public void setReset(Boolean reset) {
		this.reset = reset;
	}

	public DateTime getLastLogin() {
		if (lastLogin != null) {
			DateTimeZone tz = DateTimeZone.forID("America/Chicago");
			DateTime dt = new DateTime(lastLogin);
			return dt.withZone(tz);
		} else {
			return null;
		}
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public List<String> getUserRoleIds() {
		return userRoleIds;
	}

	public void setUserRoleIds(List<String> userRoleIds) {
		this.userRoleIds = userRoleIds;
	}

	public Set<UserOrganization> getUserOrganizations() {
		return userOrganizations;
	}

	public void setUserOrganizations(Set<UserOrganization> userOrganizations) {
		this.userOrganizations = userOrganizations;
	}

	public List<Long> getUserOrganizationIds() {
		return userOrganizationIds;
	}

	public void setUserOrganizationIds(List<Long> userOrganizationIds) {
		this.userOrganizationIds = userOrganizationIds;
	}
}
