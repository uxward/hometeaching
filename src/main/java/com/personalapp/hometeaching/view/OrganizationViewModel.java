package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Organization;

public class OrganizationViewModel {

	private Long id;

	private String organization;

	public OrganizationViewModel(Organization organization) {
		this.id = organization.getId();
		this.organization = organization.getOrganization();
	}

	public Long getId() {
		return id;
	}

	public String getOrganization() {
		return organization;
	}
}
