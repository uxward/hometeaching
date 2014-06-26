package com.personalapp.hometeaching.service;

import java.util.List;

import com.personalapp.hometeaching.model.FamilyNote;
import com.personalapp.hometeaching.view.NoteViewModel;

public interface NoteService {

	NoteViewModel save(FamilyNote note);

	List<NoteViewModel> getByFamily(Long familyId);
}
