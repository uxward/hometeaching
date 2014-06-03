package com.personalapp.hometeaching.service;

import com.personalapp.hometeaching.model.Recovery;

public interface RecoveryService {
	Recovery findByToken(String token);

	void save(Recovery recovery);
	
	void remove(Recovery recovery);
}
