package com.newtonk.action;

import com.newtonk.entity.User;
import com.newtonk.service.IUserService;
import com.newtonk.util.MessageDigestUtils;
import com.newtonk.util.SendMail;
import com.newtonk.util.UserIdentity;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {
	private User user;
	private IUserService service;
	private String name;

	public void setService(IUserService service) {
		this.service = service;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/*
	 * ��½
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		ActionContext context = ActionContext.getContext();
		if (context.getSession().get("user") != null) {
			return SUCCESS;// �ѵ�¼
		}
		if (user == null) {
			return ERROR;
		}
		if (user.getName() == null || user.getName().length() > 7
				|| user.getName().length() < 2 || user.getPassword() == null
				|| user.getPassword().length() < 4
				|| user.getPassword().length() > 12) {
			addActionError("�Ƿ�����");
			return ERROR;
		}
		user.setPassword(MessageDigestUtils.sha1(user.getPassword()));// sha1����;
		User getUser = service.login(user);
		if (getUser != null) {
			context.getSession().put("user", getUser);// sessionֲ����Ϣ
			return SUCCESS;
		} else {// �û����������
			addActionError("�û������������");
			return ERROR;
		}
	}

	/**
	 * ע��
	 * 
	 * @return
	 * @throws Exception
	 */
	public String regist() throws Exception {
		if (!vilidate(user)) {
			return ERROR;// ҳ�汻�۸�
		}
		user.setPassword(MessageDigestUtils.sha1(user.getPassword()));// sha15����);
		user.setIdentity(UserIdentity.FANS);
		boolean result = service.regist(user);
		if (result) {// �ظ��ˣ�����ע��
			addActionMessage("ע��ɹ������½");
			return SUCCESS;
		} else {// ע��ɹ�
			addActionError("�����˺ű���ע�ˡ���");
			return ERROR;
		}
	}

	/**
	 * ������תҳ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {
		if (name == null || name.length() == 0) {
			// �Ƿ����URL
			return ERROR;
		}
		ActionContext context = ActionContext.getContext();
		User user = (User) context.getSession().get("user");// sessionֲ����Ϣ,��Сʱ����Ч
		if (user == null) {
			// session����
			addActionError("ע����Ϣ���ڣ�������ע��");
			return ERROR;
		}
		String uname = MessageDigestUtils.sha1(user.getName());
		if (!uname.equals(name)) {
			addActionError("��������ע��");// �Ƿ�����
			return ERROR;
		}
		this.user = user;
		return SUCCESS;
	}

	/**
	 * �����ʼ� user(name,eamil)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String sendMail() throws Exception {
		if (user == null) {
			return ERROR;
		}
		if (user.getName() == null || user.getName().length() > 7
				|| user.getName().length() < 2 || user.getEmail() == null
				|| user.getEmail().length() == 0) {
			addActionError("�Ƿ�����");
			return ERROR;
		}
		// ����������ʽ
		String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		if (!user.getEmail().matches(regex)) {
			addActionError("���䲻��ȷ");
			return ERROR;
		}
		// ����û����Ƿ��ظ�
		boolean result = service.check(user);
		if (result) {
			addActionError("�û��Ѵ���");
			return ERROR;
		} else {// ���ظ��û��� sendemail
			ActionContext context = ActionContext.getContext();
			context.getSession().put("user", user);// sessionֲ����Ϣ,��Сʱ����Ч
			String shname = MessageDigestUtils.sha1(user.getName());
			String location = "http://localhost/Chatroom/main/detail.action?name="
					+ shname;
			SendMail.send(user.getEmail(), location);
			addActionMessage("�ѷ����ʼ����������䣬��ע�����");
			return SUCCESS;
		}
	}

	/**
	 * ��֤�����ʽ
	 * 
	 * @param user
	 * @return
	 */
	public boolean vilidate(User user) {
		if (user == null) {
			return false;
		}
		// �û��� 2-7�ֽ�
		// ����4-12
		if (user.getName() == null || user.getName().length() > 7
				|| user.getName().length() < 2) {
			return false;
		}
		if (user.getPassword() == null || user.getPassword().length() < 4
				|| user.getPassword().length() > 12) {
			return false;
		}
		String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		if (!user.getEmail().matches(regex)) {
			return false;
		}
		return true;
	}
}
