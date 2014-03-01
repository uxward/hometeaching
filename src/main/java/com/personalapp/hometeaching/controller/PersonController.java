package com.personalapp.hometeaching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalapp.hometeaching.model.Person;
import com.personalapp.hometeaching.service.PersonService;
import com.personalapp.hometeaching.view.DatatableResponse;
import com.personalapp.hometeaching.view.PersonViewModel;

@Controller
@RequestMapping(value = "/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@RequestMapping(value = "/save")
	@ResponseBody
	public PersonViewModel savePerson(Person person) {
		if (person.getId() == null) {
			personService.save(person);
		} else {
			personService.edit(person);
		}
		return new PersonViewModel(person, false, false);
	}

	@RequestMapping(value = "/get")
	@ResponseBody
	public PersonViewModel getPerson(@RequestParam("personId") Long id) {
		return personService.findDetailedById(id);
	}

	@RequestMapping(value = "/getByFamily/{id}")
	@ResponseBody
	public DatatableResponse<PersonViewModel> getByFamily(@PathVariable Long id) {
		return new DatatableResponse<PersonViewModel>(personService.getByFamilyId(id));
	}
}
