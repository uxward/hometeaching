package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.view.CompanionViewModel;
import com.personalapp.hometeaching.view.FamilyViewModel;

public interface CompanionService {
	CompanionViewModel addCompanion(CompanionViewModel viewModel);

	List<FamilyViewModel> addAssignment(CompanionViewModel viewModel);

	Boolean removeAssignment(Long companionId, Long familyId);

	Boolean removeCompanion(Long companionId);

	CompanionViewModel editAssignment(CompanionViewModel viewModel);

	List<CompanionViewModel> getViewModelAllCompanionsAndActiveFamilies();

	CompanionViewModel findDetailedById(Long id);

	List<Companion> getAllCompanionsAndActiveFamilies();
}
