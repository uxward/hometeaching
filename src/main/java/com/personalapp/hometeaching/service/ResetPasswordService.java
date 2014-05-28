package com.personalapp.hometeaching.service;

import com.personalapp.hometeaching.model.ResetPassword;

public interface ResetPasswordService {
	ResetPassword findByToken(String token);

	void save(ResetPassword reset);
}
