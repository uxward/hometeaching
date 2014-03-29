package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.Organization.fromId;
import static com.personalapp.hometeaching.model.Role.fromRole;

import java.util.List;

import org.joda.time.DateTime;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.UserOrganization;
import com.personalapp.hometeaching.model.UserRole;

public class UserViewModel extends ActionViewModel {
	private Long id;

	private Long personId;

	private String name;

	private String username;

	private Boolean reset;

	private DateTime lastLogin;

	private String email;

	private Boolean enabled;

	private Long companionId;

	private List<RoleViewModel> roles = newArrayList();

	private List<OrganizationViewModel> organizations = newArrayList();

	public UserViewModel() {

	}

	public UserViewModel(HometeachingUser user) {
		createViewModelFromUser(user);
	}

	public void createViewModelFromUser(HometeachingUser user) {
		this.id = user.getId();
		this.personId = user.getPerson().getId();
		this.name = user.getPerson().getFirstName() + " " + user.getPerson().getFamily().getFamilyName();
		this.username = user.getUsername();
		this.reset = user.getReset();
		this.lastLogin = user.getLastLogin();
		this.email = user.getEmail();
		this.enabled = user.getEnabled();
		if (user.getPerson() != null && user.getPerson().getActiveHomeTeachingCompanion() != null) {
			this.companionId = user.getPerson().getActiveHomeTeachingCompanion().getCompanionId();
		}

		for (UserRole userRole : user.getUserRoles()) {
			this.roles.add(new RoleViewModel(fromRole(userRole.getRole())));
		}

		for (UserOrganization userOrg : user.getUserOrganizations()) {
			this.organizations.add(new OrganizationViewModel(fromId(userOrg.getOrganizationId()), null));
		}
	}

	public Long getId() {
		return id;
	}

	public Long getPersonId() {
		return personId;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public Boolean getReset() {
		return reset;
	}

	public DateTime getLastLogin() {
		return lastLogin;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Long getCompanionId() {
		return companionId;
	}

	public List<RoleViewModel> getRoles() {
		return roles;
	}

	public List<OrganizationViewModel> getOrganizations() {
		return organizations;
	}
}
