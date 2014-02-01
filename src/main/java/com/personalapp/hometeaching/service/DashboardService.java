package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.VisitPercentageViewModel;

public interface DashboardService {

	List<VisitPercentageViewModel> getVisitPercentage();

	List<FamilyStatusViewModel> getFamilyStatusPercentage();

	List<FamilyStatusViewModel> getVisitPercentageDetails(Integer month, Integer year);
}
