package com.newtonk.entity;

import java.io.Serializable;
import java.util.List;

public class OnlineDate implements Serializable {
	private int online_num;
	private List<String> names;

	public int getOnline_num() {
		return online_num;
	}

	public void setOnline_num(int online_num) {
		this.online_num = online_num;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public OnlineDate(int online_num, List<String> names) {
		this.online_num = online_num;
		this.names = names;
	}

	public OnlineDate() {
	}

}
