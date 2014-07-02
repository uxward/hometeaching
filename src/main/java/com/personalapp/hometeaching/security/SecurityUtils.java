package com.personalapp.hometeaching.security;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.Organization.ELDERS;
import static com.personalapp.hometeaching.model.Organization.HIGH_PRIEST;
import static com.personalapp.hometeaching.model.Organization.RELIEF_SOCIETY;
import static com.personalapp.hometeaching.model.Organization.WARD;
import static com.personalapp.hometeaching.model.Role.ADMIN;
import static com.personalapp.hometeaching.model.Role.COUNCIL;
import static com.personalapp.hometeaching.model.Role.TEACHER;
import static com.personalapp.hometeaching.model.Role.LEADER;
import static com.personalapp.hometeaching.model.Role.MEMBERSHIP;
import static com.personalapp.hometeaching.model.Role.fromRole;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyNote;
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
		return authentication != null && !StringUtils.equals(authentication.getName(), "anonymousUser") ? (HometeachingUserDetails) authentication.getPrincipal() : null;
	}

	public static boolean currentUserHasRole(Role role) {
		switch (role) {
		case ADMIN:
			return currentUserIsAdmin();
		case LEADER:
			return currentUserIsLeader();
		case COUNCIL:
			return currentUserIsCouncil();
		case MEMBERSHIP:
			return currentUserIsMembership();
		case TEACHER:
			return currentUserIsHometeacher();
		default:
			return false;
		}
	}

	public static List<Role> getCurrentUserAssignableRoles() {
		List<Role> roles = newArrayList();
		if (currentUserIsAdmin()) {
			roles = newArrayList(ADMIN, LEADER, TEACHER, MEMBERSHIP, COUNCIL);
		} else if (currentUserIsCouncil()) {
			roles = newArrayList(COUNCIL, LEADER, MEMBERSHIP, TEACHER);
		} else if (currentUserIsLeader()) {
			roles = newArrayList(LEADER, TEACHER);
		} else if (currentUserIsMembership()) {
			roles = newArrayList(MEMBERSHIP, TEACHER);
		} else {
			roles = newArrayList(TEACHER);
		}
		return roles;
	}

	public static boolean hasFamilyAccess(Family family) {
		return currentUserIsMembership() || Objects.equal(family.getId(), getCurrentUser().getFamily().getId()) || currentUserIsTeacher(family);
	}

	public static boolean hasCompanionAccess(Companion companion) {
		return currentUserIsCouncil() || currentUserIsInCompanion(companion);
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
		return currentUserIsInCompanion(companion) || currentUserIsAdmin() || currentUserIsLeader() && companionInCurrentUserOrganizations(companion);
	}

	public static boolean canViewNote(FamilyNote note) {
		return currentUserHasRole(fromRole(note.getVisibleRole()));
	}

	private static boolean currentUserIsTeacher(Family family) {
		for (Companion companion : getCurrentUser().getActiveCompanions()) {
			for (Assignment assignment : companion.getAssignments()) {
				if (Objects.equal(family.getId(), assignment.getFamily().getId())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean companionInCurrentUserOrganizations(Companion companion) {
		return getCurrentUserOrganizationIds().contains(companion.getOrganization().getId());
	}

	private static boolean currentUserIsInCompanion(Companion companion) {
		for (Companion currentUserCompanion : getCurrentUser().getActiveCompanions()) {
			if (Objects.equal(currentUserCompanion.getId(), companion.getId())) {
				return true;
			}
		}
		return false;
	}

	private static boolean currentUserIsAdmin() {
		return currentUserIsRole(ADMIN);
	}

	private static boolean currentUserIsLeader() {
		return currentUserIsRole(LEADER) || currentUserIsAdmin();
	}

	private static boolean currentUserIsCouncil() {
		return currentUserIsRole(COUNCIL) || currentUserIsLeader();
	}

	private static boolean currentUserIsMembership() {
		return currentUserIsRole(MEMBERSHIP) || currentUserIsCouncil();
	}

	private static boolean currentUserIsHometeacher() {
		return currentUserIsRole(TEACHER) || currentUserIsMembership();
	}

	private static boolean currentUserIsRole(Role role) {
		boolean hasRole = false;
		for (GrantedAuthority grantedAuthority : getCurrentUser().getAuthorities()) {
			if (grantedAuthority.getAuthority().equalsIgnoreCase(role.getRole())) {
				hasRole = true;
				break;
			}
		}
		return hasRole;
	}
}
