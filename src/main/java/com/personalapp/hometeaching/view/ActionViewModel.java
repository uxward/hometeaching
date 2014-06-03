package com.personalapp.hometeaching.view;

import static com.personalapp.hometeaching.model.ActionStatus.DUPLICATE;
import static com.personalapp.hometeaching.model.ActionStatus.ERROR;
import static com.personalapp.hometeaching.model.ActionStatus.NOT_FOUND;
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

	public boolean getSuccess() {
		return SUCCESS.equals(actionStatus);
	}

	public boolean getDuplicate() {
		return DUPLICATE.equals(actionStatus);
	}

	public boolean getError() {
		return ERROR.equals(actionStatus);
	}
	
	public boolean getNotFound(){
		return NOT_FOUND.equals(actionStatus);
	}

}
