package com.personalapp.hometeaching.security;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	public String getPassword(String password) {
		return bcryptPasswordEncoder.encode(password);
	}

	public boolean matchPassword(String password) {
		return BCrypt.checkpw(password, getCurrentUser().getPassword());
	}

}
