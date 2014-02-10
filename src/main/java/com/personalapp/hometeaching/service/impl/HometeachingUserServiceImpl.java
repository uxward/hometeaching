package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.model.UserOrganization;
import com.personalapp.hometeaching.model.UserRole;
import com.personalapp.hometeaching.repository.HometeachingUserRepository;
import com.personalapp.hometeaching.security.PasswordUtils;
import com.personalapp.hometeaching.security.SecurityUtils;
import com.personalapp.hometeaching.service.HometeachingUserService;
import com.personalapp.hometeaching.view.UserViewModel;

@Service
public class HometeachingUserServiceImpl implements HometeachingUserService {

	@Autowired
	private HometeachingUserRepository repo;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private SecurityUtils securityUtils;

	@Override
	public UserViewModel save(HometeachingUser user) {
		// TODO catch when constraint exception violated (same username/same
		// password)
		user.setEnabled(true);
		user.setReset(true);
		user.setPassword(passwordUtils.getPassword(user.getPassword()));

		// set roles
		setUserRoles(user, user.getUserRoleIds());

		// set organization
		setUserOrganizations(user, user.getUserOrganizationIds());

		repo.save(user);

		return new UserViewModel(repo.findDetailedById(user.getId()));
	}

	@Override
	public List<UserViewModel> getAllUsers() {
		List<UserViewModel> users = newArrayList();
		for (HometeachingUser user : repo.getAllUsers()) {
			users.add(new UserViewModel(user));
		}
		return users;
	}

	@Override
	public UserViewModel getUserDetails(Long userId) {
		return new UserViewModel(repo.findDetailedById(userId));
	}

	@Override
	public UserViewModel toggleEnabled(Long id) {
		HometeachingUser user = repo.findDetailedById(id);
		user.setEnabled(!user.getEnabled());
		repo.update(user);
		return new UserViewModel(user);
	}

	@Override
	public UserViewModel update(HometeachingUser updatedUser) {
		HometeachingUser user = repo.findDetailedById(updatedUser.getId());
		if (securityUtils.usernameNotUsed(user.getUsername(), user)) {
			user.setUsername(updatedUser.getUsername());
			if (updatedUser.getReset() != null && updatedUser.getReset()) {
				user.setPassword(passwordUtils.getPassword(updatedUser.getPassword()));
				user.setReset(true);
			}

			user.setEmail(updatedUser.getEmail());

			// set user roles
			setUserRoles(user, updatedUser.getUserRoleIds());

			// set user organizations
			setUserOrganizations(user, updatedUser.getUserOrganizationIds());

			repo.update(user);
		}
		return new UserViewModel(user);
	}

	private void setUserRoles(HometeachingUser user, List<String> roles) {
		user.setUserRoles(new HashSet<UserRole>());
		for (String role : roles) {
			UserRole userRole = new UserRole();
			userRole.setRole(role);
			userRole.setHometeachingUser(user);
			user.getUserRoles().add(userRole);
		}
		getCurrentUser().getHometeachingUser().setUserRoles(user.getUserRoles());
	}

	private void setUserOrganizations(HometeachingUser user, List<Long> ids) {
		user.setUserOrganizations(new HashSet<UserOrganization>());
		for (Long id : ids) {
			UserOrganization org = new UserOrganization();
			org.setHometeachingUser(user);
			org.setOrganization(Organization.fromId(id));
			user.getUserOrganizations().add(org);
		}
		getCurrentUser().getHometeachingUser().setUserOrganizations(user.getUserOrganizations());
	}
}
