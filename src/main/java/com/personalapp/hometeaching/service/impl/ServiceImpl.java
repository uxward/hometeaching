package com.personalapp.hometeaching.service.impl;

import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.view.ActionViewModel;

@Service
public class ServiceImpl implements com.personalapp.hometeaching.service.Service {

	@Override
	public ActionViewModel setupActionViewModel(ActionStatus status) {
		ActionViewModel viewStatus = new ActionViewModel();
		viewStatus.setActionStatus(status);
		return viewStatus;
	}
}
