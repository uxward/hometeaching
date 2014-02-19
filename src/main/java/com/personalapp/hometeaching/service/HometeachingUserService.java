package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.view.UserViewModel;

public interface HometeachingUserService {

	UserViewModel save(HometeachingUser user);

	UserViewModel getUserDetails(Long userId);

	UserViewModel toggleEnabled(Long id);

	UserViewModel update(HometeachingUser user);

	List<UserViewModel> getAllUsers();

	List<HometeachingUser> getAllUsersToEmail();
}
