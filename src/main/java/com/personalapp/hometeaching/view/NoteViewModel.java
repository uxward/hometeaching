package com.personalapp.hometeaching.view;

import static com.personalapp.hometeaching.model.Role.fromRole;

import com.personalapp.hometeaching.model.FamilyNote;

public class NoteViewModel {

	private Long id;

	private String note;

	private RoleViewModel role;
	
	private UserViewModel author;

	public NoteViewModel(FamilyNote note) {
		this.note = note.getNote();
		this.role = new RoleViewModel(fromRole(note.getVisibleRole()));
	}

	public NoteViewModel() {

	}

	public Long getId() {
		return id;
	}

	public String getNote() {
		return note;
	}

	public RoleViewModel getRole() {
		return role;
	}
}
