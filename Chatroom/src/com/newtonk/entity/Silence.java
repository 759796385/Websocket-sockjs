package com.newtonk.entity;

import java.io.Serializable;

public class Silence implements Serializable {
	private int sid;
	private User user;

	public Silence() {
	}

	public Silence(User user) {
		this.user = user;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
