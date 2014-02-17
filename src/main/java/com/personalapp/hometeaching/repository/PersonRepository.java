package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.Person;

public interface PersonRepository extends Repository<Person, Long> {
	Person findDetailedById(Long id);

	List<Person> getAllNotMovedHometeachers();

	List<Person> getUnassignedHometeachingUsers();

	List<Person> getByFamilyId(Long id);
}
