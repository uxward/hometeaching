package com.personalapp.hometeaching.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalapp.hometeaching.model.FamilyNote;
import com.personalapp.hometeaching.repository.NoteRepository;
import com.personalapp.hometeaching.security.SecurityUtils;
import com.personalapp.hometeaching.service.NoteService;
import com.personalapp.hometeaching.view.NoteViewModel;

@Service
public class NoteServiceImpl implements NoteService {
	private final Logger logger = getLogger(getClass());

	@Autowired
	private NoteRepository repo;

	@Override
	public NoteViewModel save(FamilyNote note) {
		if (note.getId() == null) {
			repo.save(note);
		} else {
			repo.update(note);
		}
		return new NoteViewModel(note);
	}

	@Override
	public List<NoteViewModel> getByFamily(Long familyId) {
		List<NoteViewModel> notes = newArrayList();
		for (FamilyNote note : repo.getByFamilyId(familyId)) {
			if (SecurityUtils.canViewNote(note)) {
				notes.add(new NoteViewModel(note));
			}
		}
		return notes;
	}
}
