package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserOrganizations;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUserRoles;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.personalapp.hometeaching.model.HometeachingUser;
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
	private final Logger logger = getLogger(getClass());

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
		logger.info("User " + getCurrentUser().getPerson().getFullName() + " is viewing all users");
		ModelAndView view = new ModelAndView("user/users");
		view.addObject("organizations", getCurrentUserOrganizations());
		view.addObject("roles", getCurrentUserRoles());
		view.addObject("unassigned", personRepo.getUnassignedHometeachingUsers());
		return view;
	}

	@RequestMapping(value = "getAllUsers")
	@ResponseBody
	public DatatableResponse<UserViewModel> getAllUsers() {
		return new DatatableResponse<UserViewModel>(userService.getAllUsers());
	}

	@RequestMapping(value = "/you")
	public ModelAndView viewYou() {
		return new ModelAndView("user/detail", "user", getCurrentUser().getHometeachingUser());
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	public UserViewModel save(HometeachingUser user) {
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
