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
	 * 登陆
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		ActionContext context = ActionContext.getContext();
		if (context.getSession().get("user") != null) {
			return SUCCESS;// 已登录
		}
		if (user == null) {
			return ERROR;
		}
		if (user.getName() == null || user.getName().length() > 7
				|| user.getName().length() < 2 || user.getPassword() == null
				|| user.getPassword().length() < 4
				|| user.getPassword().length() > 12) {
			addActionError("非法输入");
			return ERROR;
		}
		user.setPassword(MessageDigestUtils.sha1(user.getPassword()));// sha1加密;
		User getUser = service.login(user);
		if (getUser != null) {
			context.getSession().put("user", getUser);// session植入信息
			return SUCCESS;
		} else {// 用户名密码错误
			addActionError("用户名或密码错误");
			return ERROR;
		}
	}

	/**
	 * 注册
	 * 
	 * @return
	 * @throws Exception
	 */
	public String regist() throws Exception {
		if (!vilidate(user)) {
			return ERROR;// 页面被篡改
		}
		user.setPassword(MessageDigestUtils.sha1(user.getPassword()));// sha15加密);
		user.setIdentity(UserIdentity.FANS);
		boolean result = service.regist(user);
		if (result) {// 重复了，重新注册
			addActionMessage("注册成功，请登陆");
			return SUCCESS;
		} else {// 注册成功
			addActionError("您的账号被抢注了。。");
			return ERROR;
		}
	}

	/**
	 * 邮箱跳转页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {
		if (name == null || name.length() == 0) {
			// 非法获得URL
			return ERROR;
		}
		ActionContext context = ActionContext.getContext();
		User user = (User) context.getSession().get("user");// session植入信息,半小时内有效
		if (user == null) {
			// session过期
			addActionError("注册信息过期，请重新注册");
			return ERROR;
		}
		String uname = MessageDigestUtils.sha1(user.getName());
		if (!uname.equals(name)) {
			addActionError("请先正常注册");// 非法入侵
			return ERROR;
		}
		this.user = user;
		return SUCCESS;
	}

	/**
	 * 发送邮件 user(name,eamil)
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
			addActionError("非法输入");
			return ERROR;
		}
		// 邮箱正则表达式
		String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		if (!user.getEmail().matches(regex)) {
			addActionError("邮箱不正确");
			return ERROR;
		}
		// 检测用户名是否重复
		boolean result = service.check(user);
		if (result) {
			addActionError("用户已存在");
			return ERROR;
		} else {// 无重复用户名 sendemail
			ActionContext context = ActionContext.getContext();
			context.getSession().put("user", user);// session植入信息,半小时内有效
			String shname = MessageDigestUtils.sha1(user.getName());
			String location = "http://localhost/Chatroom/main/detail.action?name="
					+ shname;
			SendMail.send(user.getEmail(), location);
			addActionMessage("已发送邮件到您的邮箱，请注意查收");
			return SUCCESS;
		}
	}

	/**
	 * 验证输入格式
	 * 
	 * @param user
	 * @return
	 */
	public boolean vilidate(User user) {
		if (user == null) {
			return false;
		}
		// 用户名 2-7字节
		// 密码4-12
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
