package com.newtonk.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MySecurity extends Authenticator {
	private String name;
	private String password;

	public MySecurity(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public MySecurity() {
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

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {// 返回验证信息
		return new PasswordAuthentication(name, password);
	}
}
