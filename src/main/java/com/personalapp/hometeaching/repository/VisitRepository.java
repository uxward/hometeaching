package com.personalapp.hometeaching.repository;

import java.util.List;

import com.mysema.query.Tuple;
import com.personalapp.hometeaching.model.Visit;

public interface VisitRepository extends Repository<Visit, Long> {

	List<Tuple> getAllVisitsByOrganization(Long organizationId);

	List<Visit> getVisitsByFamilyId(Long familyId, boolean visitingTeaching);
}
