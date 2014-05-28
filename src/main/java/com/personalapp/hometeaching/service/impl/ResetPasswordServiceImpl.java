package com.personalapp.hometeaching.service.impl;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.ResetPassword;
import com.personalapp.hometeaching.repository.ResetPasswordRepository;
import com.personalapp.hometeaching.service.ResetPasswordService;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private ResetPasswordRepository resetPasswordRepo;

	@Override
	public ResetPassword findByToken(String token) {
		return resetPasswordRepo.findByToken(token);
	}

	@Override
	public void save(ResetPassword reset) {
		resetPasswordRepo.save(reset);
	}
}
