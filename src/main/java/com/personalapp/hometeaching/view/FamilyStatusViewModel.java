package com.personalapp.hometeaching.view;

import com.mysema.query.Tuple;
import com.personalapp.hometeaching.model.FamilyStatus;

public class FamilyStatusViewModel {

	private String familyStatus;

	private Long totalFamilies;

	private Double familyPercent;

	private Long totalVisited;

	private Double percentVisited;

	public FamilyStatusViewModel(Tuple tupleStatus, Long familyCount) {
		Object[] arrayStatus = tupleStatus.toArray();
		this.totalFamilies = (Long) arrayStatus[0];
		Long familyStatusId = (Long) arrayStatus[1];
		this.familyStatus = FamilyStatus.fromId(familyStatusId).getStatus();
		this.familyPercent = (double) totalFamilies / (double) familyCount;
		if (arrayStatus.length > 2 && arrayStatus[2] != null) {
			this.percentVisited = (double) arrayStatus[2];
			this.totalVisited = Math.round(percentVisited * totalFamilies);
		}
	}

	public String getFamilyStatus() {
		return familyStatus;
	}

	public Long getTotalFamilies() {
		return totalFamilies;
	}

	public Double getFamilyPercent() {
		return familyPercent;
	}

	public Long getTotalVisited() {
		return totalVisited;
	}

	public Double getPercentVisited() {
		return percentVisited;
	}
}
