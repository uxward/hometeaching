package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.currentUserIsAdmin;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.model.Role;
import com.personalapp.hometeaching.repository.HometeachingUserRepository;
import com.personalapp.hometeaching.repository.PersonRepository;
import com.personalapp.hometeaching.security.PasswordUtils;
import com.personalapp.hometeaching.security.SecurityUtils;
import com.personalapp.hometeaching.service.HometeachingUserService;
import com.personalapp.hometeaching.view.DatatableResponse;
import com.personalapp.hometeaching.view.UserViewModel;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private HometeachingUserRepository userRepo;

	@Autowired
	private PersonRepository personRepo;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private SecurityUtils securityUtils;

	@Autowired
	private HometeachingUserService userService;

	@RequestMapping("/all")
	public ModelAndView viewAll(Model model) {
		ModelAndView view = new ModelAndView("user/users");
		view.addObject("organizations", Organization.values());
		view.addObject("roles", Role.values());
		view.addObject("unassigned", personRepo.getUnassignedHometeachingUsers());
		return view;
	}

	@RequestMapping(value = "getAllUsers")
	@ResponseBody
	public DatatableResponse<UserViewModel> getAllUsers() {
		return new DatatableResponse<UserViewModel>(userService.getAllUsers());
	}

	@RequestMapping("/all/detail{id}")
	public ModelAndView viewSingle(@PathVariable Long id) {
		ModelAndView view;
		if (currentUserIsAdmin()) {
			view = new ModelAndView("user/detail", "user", userRepo.findDetailedById(id));
		} else {
			view = new ModelAndView("denied");
		}
		return view;
	}

	@RequestMapping(value = "/you")
	public ModelAndView viewYou() {
		return new ModelAndView("user/detail", "user", getCurrentUser().getHometeachingUser());
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public UserViewModel save(HometeachingUser user) {
		// TODO catch when constraint exception violated (same username/same
		// password)
		return userService.save(user);
	}

	@RequestMapping(value = "/update")
	public ModelAndView udpate(@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String password, @RequestParam("username") String username) {
		HometeachingUser hometeachingUser = getCurrentUser().getHometeachingUser();
		ModelAndView view = new ModelAndView("user/detail");

		if (securityUtils.usernameNotUsed(username)) {
			if (passwordUtils.matchPassword(oldPassword)) {
				hometeachingUser.setUsername(username);
				hometeachingUser.setPassword(passwordUtils.getPassword(password));
				hometeachingUser.setReset(false);
				userRepo.update(hometeachingUser);
				view.addObject("success", true);
			} else {
				view.addObject("passwordError", true);
			}
		} else {
			view.addObject("usernameError", true);
		}

		view.addObject("user", hometeachingUser);
		return view;
	}

	@RequestMapping(value = "getUserDetails")
	@ResponseBody
	public UserViewModel getUserDetails(@RequestParam("userId") Long userId) {
		return userService.getUserDetails(userId);
	}

	@RequestMapping(value = "updateUserDetails")
	@ResponseBody
	public UserViewModel updateUserDetails(HometeachingUser user) {
		return userService.update(user);
	}

	@RequestMapping(value = "toggleEnabled")
	@ResponseBody
	public UserViewModel toggleEnabled(@RequestParam("id") Long id) {
		return userService.toggleEnabled(id);
	}
}
