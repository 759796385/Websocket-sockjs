package com.newtonk.entity;

import java.io.Serializable;

public class User implements Serializable {
	private int uid;// id
	private String name;// ’À∫≈
	private String password;// √‹¬Î
	private String email;// ” œ‰
	private String identity;// …Ì∑›

	public User() {
	}

	public User(String name, String password, String identity) {
		this.name = name;
		this.password = password;
		this.identity = identity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
