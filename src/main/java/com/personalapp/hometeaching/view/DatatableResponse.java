package com.personalapp.hometeaching.view;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Lists;

public class DatatableResponse<T> {
	@JsonProperty(value = "aaData")
	private List<T> data = Lists.newArrayList();

	public DatatableResponse(List<T> data) {
		this.data = data;
	}

	public DatatableResponse() {
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		if (data != null) {
			this.data = data;
		}
	}
}
