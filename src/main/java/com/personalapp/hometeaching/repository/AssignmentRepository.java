package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.Assignment;

public interface AssignmentRepository extends Repository<Assignment, Long> {

	Assignment findByCompanionIdAndFamilyId(Long companionId, Long familyId);

	List<Assignment> findActiveByCompanion(Long companionId);

}
