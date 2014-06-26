package com.personalapp.hometeaching.repository;

import java.util.List;

import com.personalapp.hometeaching.model.FamilyNote;

public interface NoteRepository extends Repository<FamilyNote, Long> {

	List<FamilyNote> getByFamilyId(Long familyId);
}
