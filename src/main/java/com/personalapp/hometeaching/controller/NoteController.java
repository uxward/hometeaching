package com.personalapp.hometeaching.controller;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personalapp.hometeaching.model.FamilyNote;
import com.personalapp.hometeaching.service.NoteService;
import com.personalapp.hometeaching.view.NoteViewModel;

@Controller
@RequestMapping(value = "/note")
public class NoteController {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private NoteService service;

	@RequestMapping(value = "/save")
	@ResponseBody
	public NoteViewModel savePerson(FamilyNote note) {
		return service.save(note);
	}

	@RequestMapping(value = "/getByFamily/{id}")
	@ResponseBody
	public List<NoteViewModel> detail(@PathVariable Long id) {
		logger.info("User {} is getting notes for family with id {}", getCurrentUser().getPerson().getFullName(), id);
		return service.getByFamily(id);
	}
}
