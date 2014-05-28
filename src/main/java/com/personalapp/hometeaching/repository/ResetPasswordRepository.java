package com.personalapp.hometeaching.repository;

import com.personalapp.hometeaching.model.ResetPassword;

public interface ResetPasswordRepository extends Repository<ResetPassword, Long> {

	ResetPassword findByToken(String token);

}
