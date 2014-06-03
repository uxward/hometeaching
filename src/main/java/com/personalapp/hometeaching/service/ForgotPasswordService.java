package com.personalapp.hometeaching.service;

import com.personalapp.hometeaching.view.ActionViewModel;

public interface ForgotPasswordService extends Service {

	ActionViewModel forgotPassword(String email);

	ActionViewModel resetPassword(String passwordToken, String password);
}
