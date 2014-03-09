package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Organization;

public class OrganizationViewModel {

	private Long id;

	private String organization;

	private Long totalFamilies;

	public OrganizationViewModel(Organization organization, Long totalFamilies) {
		this.id = organization.getId();
		this.organization = organization.getOrganization();
		this.totalFamilies = totalFamilies;
	}

	public Long getId() {
		return id;
	}

	public String getOrganization() {
		return organization;
	}

	public Long getTotalFamilies() {
		return totalFamilies;
	}
}
