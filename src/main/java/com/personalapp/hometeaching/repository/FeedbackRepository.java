package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.Feedback;

public interface FeedbackRepository extends Repository<Feedback, Long> {

	List<Feedback> getAllFeedback();
}
