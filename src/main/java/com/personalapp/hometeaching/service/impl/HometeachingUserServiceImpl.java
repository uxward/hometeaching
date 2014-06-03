package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.ActionStatus.DUPLICATE;
import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.ActionStatus;
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
	private final Logger logger = getLogger(getClass());

	@Autowired
	private HometeachingUserRepository repo;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private SecurityUtils securityUtils;

	@Override
	public UserViewModel save(HometeachingUser user) {
		UserViewModel model = new UserViewModel();
		ActionStatus status = SUCCESS;
		try {
			doSave(user);
			model.createViewModelFromUser(repo.findDetailedById(user.getId()));
		} catch (Exception e) {
			if (e instanceof DataIntegrityViolationException) {
				logger.info("Attempted to create a user with the same username as an existing user: {}", user.getUsername());
				status = DUPLICATE;
			} else {
				logger.error("An unexpected error occurred while trying to save a new user: {}", e);
				status = ERROR;
			}
		}

		model.setActionStatus(status);
		return model;
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
	public HometeachingUser getUserDetails(Long userId) {
		return repo.findDetailedById(userId);
	}

	@Override
	public HometeachingUser findDetailedByUsername(String username) {
		return repo.findDetailedByUsername(username);
	}

	@Override
	public HometeachingUser findDetailedByEmail(String email) {
		return repo.findDetailedByEmail(email);
	}

	@Override
	public UserViewModel getUserViewModel(Long userId) {
		return new UserViewModel(repo.findDetailedById(userId));
	}

	@Override
	public List<HometeachingUser> getCompanionsToEmail(Long companionId) {
		return repo.getCompanionsToEmail(companionId);
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
		UserViewModel model = new UserViewModel();
		ActionStatus status = SUCCESS;
		try {
			HometeachingUser user = doUpdate(updatedUser);
			model.createViewModelFromUser(repo.findDetailedById(user.getId()));
		} catch (Exception e) {
			if (e instanceof DataIntegrityViolationException) {
				logger.info("Attempted to create a user with the same username as an existing user: {}", updatedUser.getUsername());
				status = DUPLICATE;
			} else {
				logger.error("An unexpected error occurred while trying to save a new user: {}", e);
				status = ERROR;
			}
		}

		model.setActionStatus(status);
		return model;
	}

	@Override
	public void loginUpdate(HometeachingUser user) {
		try {
			repo.update(user);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while trying to update the user from login: {}", e);
		}
	}

	private HometeachingUser doUpdate(HometeachingUser updatedUser) {
		HometeachingUser user = repo.findDetailedById(updatedUser.getId());
		user.setUsername(updatedUser.getUsername());
		if (updatedUser.getReset() != null && updatedUser.getReset()) {
			user.setReset(true);
		}

		if (isNotBlank(updatedUser.getPassword())) {
			user.setPassword(passwordUtils.getPassword(updatedUser.getPassword()));
		}

		user.setEmail(updatedUser.getEmail());

		// set user roles
		setUserRoles(user, updatedUser.getUserRoleIds());

		// set user organizations
		setUserOrganizations(user, updatedUser.getUserOrganizationIds());

		repo.update(user);
		return user;
	}

	private void doSave(HometeachingUser user) {
		user.setEnabled(true);
		user.setReset(true);
		user.setPassword(passwordUtils.getPassword(user.getPassword()));

		// set roles
		setUserRoles(user, user.getUserRoleIds());

		// set organization
		setUserOrganizations(user, user.getUserOrganizationIds());

		repo.save(user);
	}

	private void setUserRoles(HometeachingUser user, List<String> roles) {
		user.setUserRoles(new HashSet<UserRole>());
		for (String role : roles) {
			UserRole userRole = new UserRole();
			userRole.setRole(role);
			userRole.setHometeachingUser(user);
			user.getUserRoles().add(userRole);
		}
		if (getCurrentUser() != null && Objects.equals(getCurrentUser().getId(), user.getId())) {
			getCurrentUser().getHometeachingUser().setUserRoles(user.getUserRoles());
		}
	}

	private void setUserOrganizations(HometeachingUser user, List<Long> ids) {
		user.setUserOrganizations(new HashSet<UserOrganization>());
		for (Long id : ids) {
			UserOrganization org = new UserOrganization();
			org.setHometeachingUser(user);
			org.setOrganization(Organization.fromId(id));
			user.getUserOrganizations().add(org);
		}
		if (getCurrentUser() != null && Objects.equals(getCurrentUser().getId(), user.getId())) {
			getCurrentUser().getHometeachingUser().setUserOrganizations(user.getUserOrganizations());
		}
	}
}
