package com.newtonk.action;

import java.util.List;
import java.util.Map;

import com.newtonk.entity.Silence;
import com.newtonk.service.ISpeakService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SpeakAction extends ActionSupport {
	private ISpeakService service;
	private List<Silence> silences;
	private Silence silence;

	public void setService(ISpeakService service) {
		this.service = service;
	}

	/*
	 * ��ѯ���б����Ե�
	 */
	@Override
	public String execute() throws Exception {
		List<Silence> result = service.getAll();
		this.silences = result;
		return SUCCESS;
	}

	/**
	 * ��ӽ���
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		service.add(silence.getUser().getName());
		return SUCCESS;
	}

	/**
	 * ɾ��������Ա
	 * 
	 * @return
	 * @throws Exception
	 */
	public String remove() throws Exception {
		ActionContext act = ActionContext.getContext();
		Map<String, Object> params = act.getParameters();
		String[] id = (String[]) params.get("id");
		int aid = 0;
		// ���id
		if (id == null) {
			return SUCCESS;// û�в���
		} else {
			aid = Integer.parseInt(id[0]);
		}
		Silence s = new Silence();
		s.setSid(aid);
		service.remove(s);
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

}
