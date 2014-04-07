package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Organization;

public class OrganizationViewModel {

	private Long id;

	private String name;

	private String abbreviation;

	private Long totalFamilies;

	public OrganizationViewModel(Organization organization, Long totalFamilies) {
		setupViewModel(organization);
		this.totalFamilies = totalFamilies;
	}

	public OrganizationViewModel(Organization organization) {
		setupViewModel(organization);
	}

	private void setupViewModel(Organization organization) {
		this.id = organization.getId();
		this.name = organization.getName();
		this.abbreviation = organization.getAbbreviation();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public Long getTotalFamilies() {
		return totalFamilies;
	}
}
