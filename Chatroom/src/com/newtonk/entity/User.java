package com.newtonk.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
	private int uid;// id
	private String name;// 账号
	private String password;// 密码
	private String email;// 邮箱
	private Date date;// 注册日期/激活时限
	private boolean state; // 激活状态 true已激活/false未激活
	private String identity;// 身份

	public User() {
	}

	public User(String name, String password, String identity) {
		this.name = name;
		this.password = password;
		this.identity = identity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
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
