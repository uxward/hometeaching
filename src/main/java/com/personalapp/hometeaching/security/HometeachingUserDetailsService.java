package com.personalapp.hometeaching.security;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.PersonCompanion;
import com.personalapp.hometeaching.repository.HometeachingUserRepository;
import com.personalapp.hometeaching.repository.PersonCompanionRepository;

@Service
public class HometeachingUserDetailsService implements UserDetailsService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	public HometeachingUserRepository repo;

	@Autowired
	public PersonCompanionRepository personCompanionRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("User is trying to log in with username {}", username);
		HometeachingUser hometeachingUser = repo.findDetailedByUsername(username);

		if (hometeachingUser == null || !hometeachingUser.getEnabled()) {
			logger.info("No users exist with username {}", username);
			throw new UsernameNotFoundException("No such user: " + username);
		}

		logger.info("User {} has successfully logged in", hometeachingUser.getPerson().getFullName());

		hometeachingUser.setLastLogin(new Date());
		repo.update(hometeachingUser);
		List<PersonCompanion> personCompanions = personCompanionRepo.getDetailedByPersonId(hometeachingUser.getPersonId());
		hometeachingUser.getPerson().setPersonCompanion(personCompanions);

		return new HometeachingUserDetails(hometeachingUser);
	}
}
