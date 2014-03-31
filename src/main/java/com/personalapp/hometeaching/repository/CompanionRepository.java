package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.Companion;

public interface CompanionRepository extends Repository<Companion, Long> {

	Companion findDetailedById(Long id);

	List<Companion> getAllHomeTeachingCompanionsAndActiveFamilies();

	Companion getCompanionAndActiveFamilies(Long companionId);

	List<Companion> getAllVisitingTeachingCompanionsAndActiveFamilies();

	List<Companion> findAllDetailedHomeTeachingByPerson(Long personId);

	List<Companion> findAllDetailedVisitingTeachingByPerson(Long personId);

}
