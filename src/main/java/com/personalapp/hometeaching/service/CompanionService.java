package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.CompanionViewModel;
import com.personalapp.hometeaching.view.FamilyViewModel;

public interface CompanionService {
	CompanionViewModel addCompanion(Companion companion);

	FamilyViewModel addAssignment(Companion viewModel);

	ActionViewModel removeAssignment(Long companionId, Long familyId);

	ActionViewModel removeCompanion(Long companionId);

	CompanionViewModel editAssignment(Companion viewModel);

	List<CompanionViewModel> getViewModelAllCompanionsAndActiveFamilies();

	Companion findDetailedById(Long id);
	
	CompanionViewModel findDetailedCompanionViewModelById(Long id);

	List<Companion> getAllCompanionsAndActiveFamilies();

	Companion getCompanionAndActiveFamilies(Long companionId);

	CompanionViewModel getDetailedViewModelForCompanion(Companion companion);
}
