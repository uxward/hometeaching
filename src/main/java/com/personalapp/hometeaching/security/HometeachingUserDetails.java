package com.personalapp.hometeaching.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.model.UserRole;

@SuppressWarnings("serial")
public class HometeachingUserDetails implements UserDetails {
	private HometeachingUser hometeachingUser;

	public HometeachingUserDetails(HometeachingUser hometeachingUser) {
		this.hometeachingUser = hometeachingUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = Lists.newArrayList();
		for (UserRole role : hometeachingUser.getUserRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}

	public HometeachingUser getHometeachingUser() {
		return hometeachingUser;
	}

	public Person getPerson() {
		return hometeachingUser.getPerson();
	}

	public Family getFamily() {
		return hometeachingUser.getPerson().getFamily();
	}

//	public List<Companion> getActiveHomeTeachingCompanions() {
//		return hometeachingUser.getPerson().getActiveCompanions(false);
//	}
//
//	public List<Companion> getActiveVisitingTeachingCompanions() {
//		return hometeachingUser.getPerson().getActiveCompanions(true);
//	}

	public List<Companion> getActiveCompanions(boolean visitingTeaching) {
		return hometeachingUser.getPerson().getActiveCompanions(visitingTeaching);
	}
	
	public List<Companion> getActiveCompanions(){
		return hometeachingUser.getPerson().getActiveCompanions();
	}

	public Long getId() {
		return hometeachingUser.getId();
	}

	public Boolean getReset() {
		return hometeachingUser.getReset();
	}

	@Override
	public String getPassword() {
		return hometeachingUser.getPassword();
	}

	@Override
	public String getUsername() {
		return hometeachingUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
