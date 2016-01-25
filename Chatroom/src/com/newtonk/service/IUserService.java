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
	 * 检查是否能注册
	 * 
	 * @param user
	 * @return true重复 false不重复
	 */
	public boolean checkCanRegist(User user);

	/**
	 * 保存未激活用户
	 * 
	 * @param user
	 * @return
	 */
	public int saveUnActivation(User user);

	/**
	 * 加载用户
	 * 
	 * @param encodeId
	 * @return
	 */
	public User getUserById(int encodeId);
}
