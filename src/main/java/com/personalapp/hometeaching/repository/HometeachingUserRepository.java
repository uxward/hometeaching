package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.HometeachingUser;

public interface HometeachingUserRepository extends Repository<HometeachingUser, Long> {
	HometeachingUser findDetailedByUsername(String username);
	
	HometeachingUser findDetailedByEmail(String email);

	HometeachingUser findDetailedById(Long id);

	List<HometeachingUser> getAllUsers();

	List<HometeachingUser> getCompanionsToEmail(Long companionId);
}
