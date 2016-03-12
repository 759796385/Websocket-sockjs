package com.newtonk.action;

import java.util.List;

import com.newtonk.entity.OnlineDate;
import com.newtonk.util.SocketSessionUtil;
import com.opensymphony.xwork2.ActionSupport;

public class JsonAction extends ActionSupport {
	private OnlineDate result;

	/*
	 * Ajax获得在线人数
	 */
	@Override
	public String execute() throws Exception {
		int online_num = SocketSessionUtil.getSize();
		List<String> names = SocketSessionUtil.getUserName();
		OnlineDate result = new OnlineDate(online_num, names);
		this.result = result;
		return SUCCESS;
	}

	public OnlineDate getResult() {
		return result;
	}

	public void setResult(OnlineDate result) {
		this.result = result;
	}

}
