package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Visit;
import com.personalapp.hometeaching.view.VisitHistoryModel;
import com.personalapp.hometeaching.view.VisitViewModel;

public interface VisitService {

	VisitViewModel save(Visit visit);

	List<VisitViewModel> getVisitsByFamilyId(Long familyId, boolean visitingTeaching);

	List<VisitHistoryModel> getHistory(Integer n, Integer month);

	List<String> getLastNMonths(Integer n);
}
