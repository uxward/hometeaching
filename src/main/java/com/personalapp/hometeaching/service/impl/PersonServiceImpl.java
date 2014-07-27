package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.personalapp.hometeaching.model.Organization.fromId;
import static com.personalapp.hometeaching.model.Organization.isVisitingTeaching;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.repository.PersonRepository;
import com.personalapp.hometeaching.service.PersonService;
import com.personalapp.hometeaching.view.PersonViewModel;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository repo;

	@Override
	public List<PersonViewModel> getAllNotMovedTeachers(boolean visitingTeaching) {
		List<PersonViewModel> teachers = newArrayList();
		for (Person person : repo.getAllNotMovedTeachers(visitingTeaching)) {
			teachers.add(new PersonViewModel(person, true, true));
		}
		return teachers;
	}

	@Override
	public List<PersonViewModel> getAllUnassignedTeachers(Long organizationId) {
		List<PersonViewModel> teachers = newArrayList();
		for (Person person : repo.getAllNotMovedTeachers(isVisitingTeaching(fromId(organizationId)))) {
			if (person.getActiveCompanions().size() < 1) {
				teachers.add(new PersonViewModel(person, true, true));
			}
		}
		return teachers;
	}

	@Override
	public Person findDetailedById(Long id) {
		return repo.findDetailedById(id);
	}

	@Override
	public PersonViewModel findDetailedViewModelById(Long id) {
		return new PersonViewModel(findDetailedById(id), true, false);
	}

	@Override
	public List<PersonViewModel> getByFamilyId(Long id) {
		List<PersonViewModel> people = newArrayList();
		for (Person person : repo.getByFamilyId(id)) {
			people.add(new PersonViewModel(person, false, false));
		}
		return people;
	}

	@Override
	public void edit(Person person) {
		setNullAsFalse(person);
		repo.update(person);
	}

	@Override
	public void save(Person person) {
		setNullAsFalse(person);
		repo.save(person);
	}

	private void setNullAsFalse(Person person) {
		if (person.getFemale() == null) {
			person.setFemale(false);
		}

		if (person.getHeadOfHousehold() == null) {
			person.setHeadOfHousehold(false);
		}

		if (person.getHomeTeacher() == null) {
			person.setHomeTeacher(false);
		}

		if (person.getVisitingTeacher() == null) {
			person.setVisitingTeacher(false);
		}

		if (person.getUser() == null) {
			person.setUser(false);
		}
	}
}
