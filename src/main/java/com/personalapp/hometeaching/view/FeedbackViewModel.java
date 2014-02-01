package com.personalapp.hometeaching.view;

import java.util.Date;

import com.personalapp.hometeaching.model.Feedback;

public class FeedbackViewModel {

	private String feedback;

	private Date date;

	private Boolean resolved;

	private PersonViewModel person;

	private PriorityViewModel priority;

	public FeedbackViewModel(Feedback feedback) {
		this.feedback = feedback.getNotes();
		this.date = feedback.getCreated();
		this.resolved = feedback.getResolved();
		this.priority = new PriorityViewModel(feedback.getPriority());
		this.person = new PersonViewModel(feedback.getUser().getPerson(), true, false);
	}

	public String getFeedback() {
		return feedback;
	}

	public Date getDate() {
		return date;
	}

	public Boolean getResolved() {
		return resolved;
	}

	public PersonViewModel getPerson() {
		return person;
	}

	public PriorityViewModel getPriority() {
		return priority;
	}
}
