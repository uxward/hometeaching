package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.Person;

public class PhoneViewModel {

	private String firstName;

	private String phoneNumber;

	public PhoneViewModel(Person person) {
		this.firstName = person.getFirstName();
		this.phoneNumber = person.getPhoneNumber();
	}

	public PhoneViewModel(Family family) {
		this.firstName = "Home";
		this.phoneNumber = family.getPhoneNumber();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getNameAndNumber() {
		return firstName + ": " + phoneNumber;
	}
}
