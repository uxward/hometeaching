package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Date;
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
	public List<PersonViewModel> getAllNotMovedNotAssignedHomeTeachers() {
		List<PersonViewModel> hometeachers = newArrayList();
		for (Person person : repo.getAllNotMovedNotAssignedHomeTeachers()) {
			hometeachers.add(new PersonViewModel(person, true, true));
		}
		return hometeachers;
	}

	@Override
	public List<PersonViewModel> getAllNotMovedNotAssignedVisitingTeachers() {
		List<PersonViewModel> hometeachers = newArrayList();
		for (Person person : repo.getAllNotMovedNotAssignedVisitingTeachers()) {
			hometeachers.add(new PersonViewModel(person, true, true));
		}
		return hometeachers;
	}

	@Override
	public PersonViewModel findDetailedById(Long id) {
		return new PersonViewModel(repo.findDetailedById(id), true, false);
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
		person.setUpdated(new Date());
		repo.update(person);
	}

	@Override
	public void save(Person person) {
		setNullAsFalse(person);
		person.setCreated(new Date());
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
