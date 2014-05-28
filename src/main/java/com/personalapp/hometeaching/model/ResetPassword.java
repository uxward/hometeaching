package com.personalapp.hometeaching.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recover")
public class ResetPassword extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "userid", insertable = false, updatable = false)
	private Long userId;

	@Column(name = "token")
	private String token;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "userid")
	private HometeachingUser user;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public HometeachingUser getUser() {
		return user;
	}

	public void setUser(HometeachingUser user) {
		this.user = user;
	}
}
