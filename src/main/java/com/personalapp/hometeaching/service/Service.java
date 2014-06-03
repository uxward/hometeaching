package com.personalapp.hometeaching.service;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.view.ActionViewModel;

public interface Service {
	ActionViewModel setupActionViewModel(ActionStatus status);
}
