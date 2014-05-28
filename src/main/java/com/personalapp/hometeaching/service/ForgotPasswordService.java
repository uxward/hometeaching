package com.personalapp.hometeaching.service;

import com.personalapp.hometeaching.view.ActionViewModel;

public interface ForgotPasswordService {

	ActionViewModel forgotPassword(String email);

	ActionViewModel resetPassword(String passwordToken, String password);
}
