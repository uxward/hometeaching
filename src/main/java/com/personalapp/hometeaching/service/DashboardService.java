package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.VisitPercentageViewModel;
import com.personalapp.hometeaching.view.WardFamilyStatusViewModel;

public interface DashboardService {

	List<VisitPercentageViewModel> getVisitPercentage();

	List<WardFamilyStatusViewModel> getFamilyStatusPercentage(List<Long> organizationIds);

	List<FamilyStatusViewModel> getVisitPercentageDetails(Integer month, Integer year);
}
