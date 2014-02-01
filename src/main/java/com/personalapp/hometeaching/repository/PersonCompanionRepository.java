package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.PersonCompanion;

public interface PersonCompanionRepository extends Repository<PersonCompanion, Long> {

	List<PersonCompanion> getDetailedByPersonId(Long id);

}
