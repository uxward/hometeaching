package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.Organization;

public interface CompanionRepository extends Repository<Companion, Long> {

	Companion findDetailedById(Long id);

	List<Companion> getAllCompanionsAndActiveFamilies(Organization organization);

	Companion getCompanionAndActiveFamilies(Long companionId);

	List<Companion> findAllDetailedCompanionsByPerson(Long personId);

}
