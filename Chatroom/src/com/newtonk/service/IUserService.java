package com.newtonk.service;

import com.newtonk.entity.User;

public interface IUserService {
	/**
	 * 登陆
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User login(User user) throws Exception;

	/**
	 * 注册
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean regist(User user) throws Exception;

	/**
	 * 检查用户名是否重复
	 * 
	 * @param user
	 * @return true重复 false不重复
	 */
	public boolean check(User user);
}
