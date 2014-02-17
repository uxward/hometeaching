package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.view.PersonViewModel;

public interface PersonService {

	void edit(Person person);

	void save(Person person);

	PersonViewModel findDetailedById(Long id);

	List<PersonViewModel> getByFamilyId(Long id);

	List<PersonViewModel> getAllNotMovedHometeachers();
}
