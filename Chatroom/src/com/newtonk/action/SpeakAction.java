package com.newtonk.action;

import java.util.List;

import com.newtonk.entity.Silence;
import com.newtonk.service.ISpeakService;
import com.opensymphony.xwork2.ActionSupport;

public class SpeakAction extends ActionSupport {
	private ISpeakService service;
	private List<Silence> silences;
	private Silence silence;
	private int id;

	public void setService(ISpeakService service) {
		this.service = service;
	}

	/*
	 * 查询所有被禁言的
	 */
	@Override
	public String execute() throws Exception {
		List<Silence> result = service.getAll();
		this.silences = result;
		return SUCCESS;
	}

	/**
	 * 添加禁言
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		try {
			service.add(silence.getUser().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 删除禁言人员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String remove() throws Exception {
		if (id == 0) {
			return SUCCESS;
		}
		Silence s = new Silence();
		s.setSid(id);
		try {
			service.remove(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public List<Silence> getSilences() {
		return silences;
	}

	public void setSilences(List<Silence> silences) {
		this.silences = silences;
	}

	public void setSilence(Silence silence) {
		this.silence = silence;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
