package com.personalapp.hometeaching.repository;

import com.personalapp.hometeaching.model.Assignment;

public interface AssignmentRepository extends Repository<Assignment, Long> {

	Assignment findByCompanionIdAndFamilyId(Long companionId, Long familyId);

}
