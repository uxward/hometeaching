package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Visit;
import com.personalapp.hometeaching.view.VisitHistoryModel;
import com.personalapp.hometeaching.view.VisitViewModel;

public interface VisitService {

	VisitViewModel save(Visit visit);

	List<VisitViewModel> getHomeTeachingVisitsByFamilyId(Long familyId);

	List<VisitHistoryModel> getVisitHistory(Integer n);

	List<String> getLastNMonths(Integer n);

	List<VisitViewModel> getVisitingTeachingVisitsByFamilyId(Long familyId);
}
