package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.view.UserViewModel;

public interface HometeachingUserService {

	UserViewModel save(HometeachingUser user);

	HometeachingUser getUserDetails(Long userId);

	HometeachingUser findDetailedByUsername(String username);
	
	HometeachingUser findDetailedByEmail(String email);

	UserViewModel getUserViewModel(Long userId);

	UserViewModel toggleEnabled(Long id);

	UserViewModel update(HometeachingUser user);

	void loginUpdate(HometeachingUser user);

	List<UserViewModel> getAllUsers();

	List<HometeachingUser> getCompanionsToEmail(Long companionId);

}
