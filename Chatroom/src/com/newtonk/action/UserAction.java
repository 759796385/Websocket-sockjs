package com.newtonk.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.newtonk.entity.User;
import com.newtonk.service.IUserService;
import com.newtonk.util.Base64Util;
import com.newtonk.util.MessageDigestUtils;
import com.newtonk.util.SendMail;
import com.newtonk.util.UserIdentity;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport implements ServletRequestAware {
	private User user;
	private IUserService service;
	private String name;
	private HttpServletRequest request;
	private String id;

	public void setService(IUserService service) {
		this.service = service;
	}

	public void setId(String id) {
		this.id = id;
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
	 * ע�� �˺ű����ڣ��޷���ע
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
		user.setDate(new Date());
		user.setState(true);
		boolean result = service.regist(user);
		if (result) {// ע��ɹ�
			addActionMessage("ע��ɹ������½");
			return SUCCESS;
		} else {// �ظ��ˣ�����ע��
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
		if (name == null || name.length() == 0 || id == null
				|| id.length() == 0) {
			// �Ƿ����URL
			return ERROR;
		}
		int encodeId = 0;
		String encodeName = null;
		try {
			encodeId = Integer.parseInt(Base64Util.decodeBase64(id));
			encodeName = Base64Util.decodeBase64(name);
		} catch (Exception e) {// ����ʧ��,���ܱ��۸�
			addActionError("�Ƿ�����");
			return ERROR;
		}
		User result = service.getUserById(encodeId);
		// ��鴫����Ϣ�Ƿ񱻴۸�
		if (!result.getName().equals(encodeName)) {
			addActionError("�Ƿ�����");
			return ERROR;
		}
		if (new Date().after(result.getDate())) {
			addActionError("�ʼ�����");
			return ERROR;
		}
		// ��ʱ��ע��
		this.user = result;
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
		regex = "^[\u4E00-\u9FA5A-Za-z0-9_]+$";
		if (!user.getName().matches(regex)) {
			addActionError("�û���������Ӣ�����ֺ��»������");
			return ERROR;
		}
		// ����û����ܷ�ע��
		boolean can = service.checkCanRegist(user);
		if (can) {// �ܹ�ע�� ����email
			int uid = service.saveUnActivation(user);
			String base64name = Base64Util.encodeBase64(user.getName());// �����û�����id
			String base64id = Base64Util.encodeBase64("" + uid);
			// ��ȡ����·��
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String location = basePath + "main/detail.action?name="
					+ base64name + "&id=" + base64id;
			SendMail.send(user.getEmail(), location);
			addActionMessage("�ѷ����ʼ����������䣬��ע�����");
			return SUCCESS;
		} else {// ����ע��
			addActionError("�û��Ѵ���");
			return ERROR;
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

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
