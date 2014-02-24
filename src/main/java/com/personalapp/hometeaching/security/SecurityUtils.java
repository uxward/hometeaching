package com.personalapp.hometeaching.security;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.Organization.ELDERS;
import static com.personalapp.hometeaching.model.Organization.HIGH_PRIEST;
import static com.personalapp.hometeaching.model.Organization.RELIEF_SOCIETY;
import static com.personalapp.hometeaching.model.Role.ADMIN;
import static com.personalapp.hometeaching.model.Role.HOMETEACHER;
import static com.personalapp.hometeaching.model.Role.LEADER;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyOrganization;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.model.Role;
import com.personalapp.hometeaching.model.UserOrganization;
import com.personalapp.hometeaching.repository.HometeachingUserRepository;

@Component
public class SecurityUtils {

	@Autowired
	private HometeachingUserRepository userRepo;

	public static HometeachingUserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null ? (HometeachingUserDetails) authentication.getPrincipal() : null;
	}

	public static boolean currentUserHasRole(Role role) {
		boolean hasRole = false;
		for (GrantedAuthority grantedAuthority : getCurrentUser().getAuthorities()) {
			if (grantedAuthority.getAuthority().equalsIgnoreCase(role.getRole())) {
				hasRole = true;
				break;
			}
		}
		return hasRole;
	}

	public static boolean currentUserIsHometeacher() {
		return currentUserHasRole(HOMETEACHER);
	}

	public static boolean currentUserIsAdmin() {
		return currentUserHasRole(ADMIN);
	}

	public static boolean currentUserIsLeader() {
		return currentUserHasRole(LEADER);
	}

	public static List<Role> getCurrentUserRoles() {
		List<Role> roles = newArrayList();
		if (currentUserIsLeader()) {
			roles = newArrayList(LEADER, HOMETEACHER);
		}
		if (currentUserIsAdmin()) {
			roles = newArrayList(ADMIN, LEADER, HOMETEACHER);
		}
		return roles;
	}

	public static boolean hasFamilyAccess(Family family) {
		return Objects.equal(family.getId(), getCurrentUser().getFamily().getId()) || (currentUserIsLeader() && familyInCurrentUserOrganizations(family));
	}

	private static boolean familyInCurrentUserOrganizations(Family family) {
		List<Long> organizationIds = newArrayList();
		for (FamilyOrganization organization : family.getFamilyOrganizations()) {
			organizationIds.add(organization.getOrganizationId());
		}
		return CollectionUtils.containsAny(organizationIds, getCurrentUserOrganizationIds());
	}

	public static boolean hasUserAccess(Long id) {
		return id.equals(getCurrentUser().getId());
	}

	public static boolean hasCompanionAccess(Long id) {
		return getCurrentUser().getActiveCompanion() != null ? id.equals(getCurrentUser().getActiveCompanion().getId()) : false;
	}

	public static List<Long> getCurrentUserOrganizationIds() {
		List<Long> organizationIds = newArrayList();
		for (UserOrganization organization : getCurrentUser().getHometeachingUser().getUserOrganizations()) {
			organizationIds.add(organization.getOrganizationId());
		}
		return organizationIds;
	}

	public static List<Organization> getCurrentUserOrganizations() {
		List<Organization> organizations = newArrayList();
		if (currentUserIsAdmin()) {
			organizations = newArrayList(RELIEF_SOCIETY, HIGH_PRIEST, ELDERS);
		}
		if (currentUserIsLeader()) {
			for (UserOrganization organization : getCurrentUser().getHometeachingUser().getUserOrganizations()) {
				organizations.add(Organization.fromId(organization.getOrganizationId()));
			}
		}
		return organizations;
	}

	public boolean usernameNotUsed(String username) {
		HometeachingUser user = userRepo.findDetailedByUsername(username);
		return user == null || user.getId() == null || user.getId().equals(getCurrentUser().getId());
	}

	public boolean usernameNotUsed(String username, HometeachingUser user) {
		HometeachingUser dbUser = userRepo.findDetailedByUsername(username);
		return dbUser == null || dbUser.getId() == null || dbUser.getId().equals(user.getId());
	}
}
