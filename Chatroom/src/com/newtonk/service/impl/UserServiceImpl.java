package com.newtonk.service.impl;

import java.util.Calendar;
import java.util.Date;

import com.newtonk.dao.IUserDAO;
import com.newtonk.entity.User;
import com.newtonk.service.IUserService;

public class UserServiceImpl implements IUserService {
	private IUserDAO dao;

	public void setDao(IUserDAO dao) {
		this.dao = dao;
	}

	/*
	 * 用户登录
	 * 
	 * @see com.newtonk.service.IUserService#login(com.newtonk.entity.User)
	 */
	@Override
	public User login(User user) throws Exception {
		return dao.Login(user);
	}

	/*
	 * 用户注册 false 用户名重复 true 注册成功
	 * 
	 * @see com.newtonk.service.IUserService#regist(com.newtonk.entity.User)
	 */
	@Override
	public boolean regist(User user) throws Exception {
		// 检查用户名是否重复
		dao.update(user);
		return true;
	}

	/**
	 * 检查能否注册 user(name,email) 用户不存在 -能注册 用户已存在&未激活&邮箱保护时间已过期-能注册
	 * 
	 * @see com.newtonk.service.IUserService#check(com.newtonk.entity.User)
	 */
	@Override
	public boolean checkCanRegist(User user) {
		// 获取用户
		User result = dao.getUserByName(user);
		if (result != null) {// 用户存在
			if (result.getState()) {// 已激活
				return false;
			} else {// 未激活
				Date expireTime = result.getDate();
				return new Date().after(expireTime);// 大于邮件过期时间便可注册
			}
		} else {// 用户不存在
			return true;
		}
	}

	/**
	 * 一小时后时间
	 * 
	 * @return
	 */
	public Date getAnHourTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(calendar.HOUR_OF_DAY, 1);
		return calendar.getTime();
	}

	/*
	 * 发送保存未激活用户 user(name,email) 用户已有纪录但未激活：更新邮箱保护时间 用户不存在-设置保护时间
	 */
	@Override
	public int saveUnActivation(User user) {
		User result = dao.getUserByName(user);// 是否有未激活用户
		if (result != null) {// 更新用户
			result.setDate(getAnHourTime());// 一小时后过期
			result.setState(false);
			return result.getUid();
		} else {// 新增用户
			user.setDate(getAnHourTime());
			user.setState(false);
			return (int) dao.save(user);
		}
	}

	/*
	 * 加载用户
	 * 
	 * @see com.newtonk.service.IUserService#getUserById(int)
	 */
	@Override
	public User getUserById(int encodeId) {
		return dao.get(User.class, encodeId);
	}
}
