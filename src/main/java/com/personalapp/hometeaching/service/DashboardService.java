package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.view.FamilyStatusViewModel;
import com.personalapp.hometeaching.view.WardFamilyStatusViewModel;
import com.personalapp.hometeaching.view.WardVisitPercentageViewModel;

public interface DashboardService {

	List<WardVisitPercentageViewModel> getVisitPercentage(List<Long> list);

	List<WardFamilyStatusViewModel> getFamilyStatusPercentage(List<Long> organizationIds);

	List<FamilyStatusViewModel> getVisitPercentageDetails(Integer month, Integer year, Long organizationId);
}
