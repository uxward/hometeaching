package com.personalapp.hometeaching.repository.impl;

import static com.personalapp.hometeaching.model.QFamilyNote.familyNote;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.personalapp.hometeaching.model.FamilyNote;
import com.personalapp.hometeaching.repository.NoteRepository;

@Repository
public class NoteRepositoryImpl extends RepositoryImpl<FamilyNote, Long> implements NoteRepository {
	private final Logger logger = getLogger(getClass());

	@Override
	public List<FamilyNote> getByFamilyId(Long familyId) {
		logger.info("Entering the get all notes by family id method with family id: {}", familyId);
		JPAQuery query = jpaFrom(familyNote);
		query.where(familyNote.familyId.eq(familyId));
		return query.list(familyNote);
	}
}
