package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.view.FamilyViewModel;

public interface FamilyService {

	FamilyViewModel edit(Family family);

	FamilyViewModel save(Family family);

	List<FamilyViewModel> getAllNotMovedFamilies();

	Family findDetailedFamilyById(Long id);

	FamilyViewModel findDetailedFamilyViewModelById(Long id);

	FamilyViewModel getDetailedViewModelForFamily(Family family);

	List<FamilyViewModel> getAllFamiliesWithoutCompanion(boolean visitingTeaching);

	List<FamilyViewModel> getAllMovedFamilies();

	List<FamilyViewModel> getAllUnknownFamilies();
}
