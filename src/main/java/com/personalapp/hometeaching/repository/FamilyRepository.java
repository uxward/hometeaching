package com.personalapp.hometeaching.repository;

import java.util.List;

import com.mysema.query.Tuple;
import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.FamilyStatus;

public interface FamilyRepository extends Repository<Family, Long> {

	List<Family> getAllNotMovedFamilies();

	List<Family> getAllMovedFamilies();

	Family findDetailedById(Long id);

	List<Family> getAllFamiliesWithoutCompanion(boolean visitingTeaching);

	List<Tuple> getFamilyStatusPercentage(List<Long> organizationIds);

	List<Family> getAllFamiliesAndVisits();

	List<Tuple> getVisitPercentageDetails(Integer month, Integer year, Long organizationId);

	List<Family> getAllNotMovedFamiliesByStatus(List<FamilyStatus> statuses);

}
