package com.newtonk.dao;

import com.newtonk.entity.User;

public interface IUserDAO extends BaseDAO<User> {
	/**
	 * ��½
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User Login(User user) throws Exception;

	/**
	 * ����û����Ƿ��ظ�
	 * 
	 * @param user
	 * @return true�ظ� false���ظ�
	 */
	public boolean check(User user);
}
