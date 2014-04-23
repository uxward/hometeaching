package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.ActionStatus.SUCCESS;
import static com.personalapp.hometeaching.model.Priority.HIGH;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Feedback;
import com.personalapp.hometeaching.repository.FeedbackRepository;
import com.personalapp.hometeaching.service.FeedbackService;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.FeedbackViewModel;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private FeedbackRepository repo;

	@Override
	public ActionViewModel createFeedback(Feedback feedback) {
		ActionStatus status = SUCCESS;
		try {
			doCreateFeedback(feedback);
		} catch (Exception e) {
			logger.error("There was an unexpected error while saving the feedback");
		}

		return createActionViewModel(status);
	}

	@Override
	public List<FeedbackViewModel> getAllFeedback() {
		List<FeedbackViewModel> feedbacks = newArrayList();
		for (Feedback feedback : repo.getAllFeedback()) {
			feedbacks.add(new FeedbackViewModel(feedback));
		}
		return feedbacks;
	}

	private void doCreateFeedback(Feedback feedback) {
		feedback.setUserId(getCurrentUser().getId());
		feedback.setPriority(HIGH);
		feedback.setResolved(false);
		repo.save(feedback);
	}

	private ActionViewModel createActionViewModel(ActionStatus status) {
		ActionViewModel model = new ActionViewModel();
		model.setActionStatus(status);
		return model;
	}
}
