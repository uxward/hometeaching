package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Feedback;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.FeedbackViewModel;

public interface FeedbackService {
	ActionViewModel createFeedback(Feedback feedback);

	List<FeedbackViewModel> getAllFeedback();
}
