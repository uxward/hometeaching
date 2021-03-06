package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.view.ActionViewModel;
import com.personalapp.hometeaching.view.CompanionViewModel;

public interface CompanionService {
	CompanionViewModel addCompanion(Companion companion);

	ActionViewModel removeCompanion(Long companionId);

	CompanionViewModel editCompanion(Companion viewModel);

	List<CompanionViewModel> getViewModelAllCompanionsAndActiveFamiliesByOrganization(Organization organization);

	Companion findDetailedById(Long id);

	List<Companion> getAllCompanionsAndActiveFamilies(Organization organization);

	Companion getCompanionAndActiveFamilies(Long companionId);

	CompanionViewModel getDetailedViewModelForCompanion(Companion companion);

	List<CompanionViewModel> getDetailedCompanionViewModelsByPersonId(Long personId, boolean visitingTeaching);
}
