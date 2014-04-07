package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.FamilyStatus.UNKNOWN;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyOrganization;
import com.personalapp.hometeaching.model.Organization;
import com.personalapp.hometeaching.repository.FamilyRepository;
import com.personalapp.hometeaching.service.FamilyService;
import com.personalapp.hometeaching.view.FamilyViewModel;

@Service
public class FamilyServiceImpl implements FamilyService {

	@Autowired
	private FamilyRepository repo;

	@Override
	public FamilyViewModel edit(Family family) {
		family.setUpdated(new Date());
		setFamilyOrganizations(family);
		setNullAsFalse(family);
		repo.update(family);
		return new FamilyViewModel(family, false, false, true);
	}

	@Override
	public FamilyViewModel save(Family family) {
		family.setCreated(new Date());
		setFamilyOrganizations(family);
		setNullAsFalse(family);
		repo.save(family);
		return new FamilyViewModel(family, false, false, true);
	}

	@Override
	public List<FamilyViewModel> getAllNotMovedFamilies() {
		List<FamilyViewModel> families = newArrayList();
		for (Family family : repo.getAllNotMovedFamilies()) {
			families.add(new FamilyViewModel(family, true, true, true));
		}
		return families;
	}

	@Override
	public List<FamilyViewModel> getAllMovedFamilies() {
		List<FamilyViewModel> families = newArrayList();
		for (Family family : repo.getAllMovedFamilies()) {
			families.add(new FamilyViewModel(family, true, true, true));
		}
		return families;
	}

	@Override
	public List<FamilyViewModel> getAllUnknownFamilies() {
		List<FamilyViewModel> families = newArrayList();
		for (Family family : repo.getAllNotMovedFamiliesByStatus(newArrayList(UNKNOWN))) {
			families.add(new FamilyViewModel(family, true, true, true));
		}
		return families;
	}

	@Override
	public Family findDetailedFamilyById(Long id) {
		return repo.findDetailedById(id);
	}

	@Override
	public FamilyViewModel findDetailedFamilyViewModelById(Long id) {
		return new FamilyViewModel(repo.findDetailedById(id), true, true, true);
	}

	@Override
	public FamilyViewModel getDetailedViewModelForFamily(Family family) {
		return new FamilyViewModel(family, true, true, true);
	}

	@Override
	public List<FamilyViewModel> getByCompanionId(Long companionId) {
		List<FamilyViewModel> families = newArrayList();
		for (Family family : repo.getByCompanionId(companionId)) {
			families.add(new FamilyViewModel(family, true, false, true));
		}
		return families;
	}

	@Override
	public List<FamilyViewModel> getAllFamiliesWithoutCompanion(boolean visitingTeaching) {
		List<FamilyViewModel> families = newArrayList();
		for (Family family : repo.getAllFamiliesWithoutCompanion(visitingTeaching)) {
			families.add(new FamilyViewModel(family, true, false, true));
		}
		return families;
	}

	private void setFamilyOrganizations(Family family) {
		family.setFamilyOrganizations(new HashSet<FamilyOrganization>());
		for (Long id : family.getFamilyOrganizationIds()) {
			FamilyOrganization org = new FamilyOrganization();
			org.setFamily(family);
			org.setOrganization(Organization.fromId(id));
			family.getFamilyOrganizations().add(org);
		}
	}

	private void setNullAsFalse(Family family) {
		if (family.getFamilyMoved() == null) {
			family.setFamilyMoved(false);
		}
		if (family.getRecordsMoved() == null) {
			family.setRecordsMoved(false);
		}
	}
}
