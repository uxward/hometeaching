package com.personalapp.hometeaching.view;

import static com.personalapp.hometeaching.model.ActionStatus.DUPLICATE;
import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;

import com.personalapp.hometeaching.model.ActionStatus;

public class ActionViewModel {

	private ActionStatus actionStatus;

	public ActionStatus getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(ActionStatus actionStatus) {
		this.actionStatus = actionStatus;
	}

	public Boolean getSuccess() {
		return SUCCESS.equals(actionStatus);
	}

	public Boolean getDuplicate() {
		return DUPLICATE.equals(actionStatus);
	}

	public Boolean getError() {
		return ERROR.equals(actionStatus);
	}

}
