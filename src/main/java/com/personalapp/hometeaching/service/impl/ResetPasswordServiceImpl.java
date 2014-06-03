package com.personalapp.hometeaching.service.impl;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.Recovery;
import com.personalapp.hometeaching.repository.RecoveryRepository;
import com.personalapp.hometeaching.service.RecoveryService;

@Service
public class ResetPasswordServiceImpl implements RecoveryService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private RecoveryRepository recoveryRepo;

	@Override
	public Recovery findByToken(String token) {
		return recoveryRepo.findByToken(token);
	}

	@Override
	public void save(Recovery reset) {
		recoveryRepo.save(reset);
	}

	@Override
	public void remove(Recovery reset) {
		recoveryRepo.remove(reset);
	}
}
