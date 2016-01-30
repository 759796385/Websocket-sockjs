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
	 * 注册 账号保护期，无法抢注
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
		user.setDate(new Date());
		user.setState(true);
		boolean result = service.regist(user);
		if (result) {// 注册成功
			addActionMessage("注册成功，请登陆");
			return SUCCESS;
		} else {// 重复了，重新注册
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
		if (name == null || name.length() == 0 || id == null
				|| id.length() == 0) {
			// 非法获得URL
			return ERROR;
		}
		int encodeId = 0;
		String encodeName = null;
		try {
			encodeId = Integer.parseInt(Base64Util.decodeBase64(id));
			encodeName = Base64Util.decodeBase64(name);
		} catch (Exception e) {// 解密失败,加密被篡改
			addActionError("非法输入");
			return ERROR;
		}
		User result = service.getUserById(encodeId);
		// 检查传递信息是否被篡改
		if (!result.getName().equals(encodeName)) {
			addActionError("非法输入");
			return ERROR;
		}
		if (new Date().after(result.getDate())) {
			addActionError("邮件过期");
			return ERROR;
		}
		// 此时可注册
		this.user = result;
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
		regex = "^[\u4E00-\u9FA5A-Za-z0-9_]+$";
		if (!user.getName().matches(regex)) {
			addActionError("用户名由中文英文数字和下划线组成");
			return ERROR;
		}
		// 检测用户名能否注册
		boolean can = service.checkCanRegist(user);
		if (can) {// 能够注册 发送email
			int uid = service.saveUnActivation(user);
			String base64name = Base64Util.encodeBase64(user.getName());// 加密用户名和id
			String base64id = Base64Util.encodeBase64("" + uid);
			// 获取访问路径
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String location = basePath + "main/detail.action?name="
					+ base64name + "&id=" + base64id;
			SendMail.send(user.getEmail(), location);
			addActionMessage("已发送邮件到您的邮箱，请注意查收");
			return SUCCESS;
		} else {// 不能注册
			addActionError("用户已存在");
			return ERROR;
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

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
