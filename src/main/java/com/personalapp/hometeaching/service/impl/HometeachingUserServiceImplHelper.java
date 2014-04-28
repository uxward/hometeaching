package com.personalapp.hometeaching.service.impl;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HometeachingUserServiceImplHelper {
	private final Logger logger = getLogger(getClass());

	@Transactional(rollbackFor = Exception.class)
	public void doResetPassword(Long companionId) {

	}
}
