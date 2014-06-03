package com.personalapp.hometeaching.repository;

import com.personalapp.hometeaching.model.Recovery;

public interface RecoveryRepository extends Repository<Recovery, Long> {

	Recovery findByToken(String token);

}
