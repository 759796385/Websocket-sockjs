package com.newtonk.service.impl;

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
		boolean result = dao.check(user);
		if (result) {
			return false;
		} else {// 用户名不重复，插入数据
			dao.save(user);
			return true;
		}
	}

	/*
	 * 检查用户名是否重复
	 * 
	 * @see com.newtonk.service.IUserService#check(com.newtonk.entity.User)
	 */
	@Override
	public boolean check(User user) {
		return dao.check(user);
	}

}
