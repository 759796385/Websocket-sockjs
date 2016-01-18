package com.newtonk.dao;

import com.newtonk.entity.User;

public interface IUserDAO extends BaseDAO<User> {
	/**
	 * 登陆
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User Login(User user) throws Exception;

	/**
	 * 检查用户名是否重复
	 * 
	 * @param user
	 * @return true重复 false不重复
	 */
	public boolean check(User user);
}
