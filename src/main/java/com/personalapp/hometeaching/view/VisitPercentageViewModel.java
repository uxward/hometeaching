package com.personalapp.hometeaching.view;

import org.joda.time.DateTime;

import com.mysema.query.Tuple;

public class VisitPercentageViewModel {

	private DateTime date;

	private Integer month;

	private Integer year;

	private Double visitPercent;

	public VisitPercentageViewModel(Tuple tupleVisit) {
		Object[] arrayVisit = tupleVisit.toArray();
		this.month = (Integer) arrayVisit[1];
		this.year = (Integer) arrayVisit[2];
		this.date = new DateTime(this.year, month, 1, 1, 1);
		this.visitPercent = (Double) arrayVisit[0];
	}

	public DateTime getDate() {
		return date;
	}

	public String getFormattedDate() {
		return date.toString("Y-M");
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getYear() {
		return year;
	}

	public Double getVisitPercent() {
		return visitPercent;
	}

}
