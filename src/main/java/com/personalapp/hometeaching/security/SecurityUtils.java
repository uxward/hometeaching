package com.personalapp.hometeaching.security;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.FamilyStatus.UNKNOWN;
import static com.personalapp.hometeaching.model.Organization.ELDERS;
import static com.personalapp.hometeaching.model.Organization.HIGH_PRIEST;
import static com.personalapp.hometeaching.model.Organization.RELIEF_SOCIETY;
import static com.personalapp.hometeaching.model.Organization.WARD;
import static com.personalapp.hometeaching.model.Role.ADMIN;
import static com.personalapp.hometeaching.model.Role.COUNCIL;
import static com.personalapp.hometeaching.model.Role.HOMETEACHER;
import static com.personalapp.hometeaching.model.Role.LEADER;
import static com.personalapp.hometeaching.model.Role.MEMBERSHIP;
import static org.apache.commons.collections.CollectionUtils.containsAny;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyOrganization;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.model.PersonCompanion;
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

	public static boolean currentUserIsAdmin() {
		return currentUserHasRole(ADMIN);
	}

	public static boolean currentUserIsLeader() {
		return currentUserHasRole(LEADER) || currentUserIsAdmin();
	}

	public static boolean currentUserIsCouncil() {
		return currentUserHasRole(COUNCIL) || currentUserIsLeader();
	}

	public static boolean currentUserIsMembership() {
		return currentUserHasRole(MEMBERSHIP) || currentUserIsCouncil();
	}

	public static boolean currentUserIsHometeacher() {
		return currentUserHasRole(HOMETEACHER) || currentUserIsMembership();
	}

	public static List<Role> getCurrentUserAssignableRoles() {
		List<Role> roles = newArrayList();
		if (currentUserIsAdmin()) {
			roles = newArrayList(ADMIN, LEADER, HOMETEACHER, MEMBERSHIP, COUNCIL);
		} else if (currentUserIsLeader()) {
			roles = newArrayList(LEADER, HOMETEACHER);
		}
		return roles;
	}

	public static boolean hasFamilyAccess(Family family) {
		return currentUserIsMembership() || Objects.equal(family.getId(), getCurrentUser().getFamily().getId());
	}

	public static boolean hasCompanionAccess(Companion companion) {
		return currentUserIsCouncil() || (getCurrentUser().getActiveCompanion() != null && companion.getId().equals(getCurrentUser().getActiveCompanion().getId()));
	}

	public static List<Long> getAllOrganizationIds() {
		return newArrayList(WARD.getId(), RELIEF_SOCIETY.getId(), HIGH_PRIEST.getId(), ELDERS.getId());
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
		} else if (currentUserIsLeader()) {
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

	public static boolean canActionCompanion(Companion companion) {
		return currentUserIsCompanion(companion) || currentUserIsAdmin() || currentUserIsLeader() && companionInCurrentUserOrganizations(companion);
	}

	private static boolean currentUserIsCompanion(Companion companion) {
		return companion != null && getCurrentUser().getActiveCompanion() != null && companion.getId().equals(getCurrentUser().getActiveCompanion().getId());
	}

	private static boolean companionInCurrentUserOrganizations(Companion companion) {
		List<Long> organizationIds = newArrayList();
		for (PersonCompanion personCompanion : companion.getCompanions()) {
			if (personCompanion.getPerson() != null && personCompanion.getPerson().getOrganization() != null) {
				organizationIds.add(personCompanion.getPerson().getOrganization().getId());
			}
		}
		return containsAny(organizationIds, getCurrentUserOrganizationIds());
	}

	private static boolean familyIsMovedOrUnknown(Family family) {
		return family.getFamilyMoved() || UNKNOWN.equals(family.getFamilyStatus());
	}

	private static boolean familyInCurrentUserOrganizations(Family family) {
		List<Long> organizationIds = newArrayList();
		for (FamilyOrganization organization : family.getFamilyOrganizations()) {
			organizationIds.add(organization.getOrganizationId());
		}
		return containsAny(organizationIds, getCurrentUserOrganizationIds());
	}
}
