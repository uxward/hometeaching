package com.personalapp.hometeaching.repository;

import java.util.List;

import com.mysema.query.Tuple;
import com.personalapp.hometeaching.model.Family;

public interface FamilyRepository extends Repository<Family, Long> {

	List<Family> getAllFamilies();

	Family findDetailedById(Long id);

	List<Family> getAllFamiliesWithoutCompanion();

	List<Tuple> getFamilyStatusPercentage();

	List<Family> getByCompanionId(Long companionId);

	List<Family> getAllFamiliesAndVisits();

	List<Tuple> getVisitPercentageDetails(Integer month, Integer year);

}
