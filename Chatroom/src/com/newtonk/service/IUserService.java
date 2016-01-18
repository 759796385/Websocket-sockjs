package com.newtonk.service;

import com.newtonk.entity.User;

public interface IUserService {
	/**
	 * ��½
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User login(User user) throws Exception;

	/**
	 * ע��
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean regist(User user) throws Exception;

	/**
	 * ����û����Ƿ��ظ�
	 * 
	 * @param user
	 * @return true�ظ� false���ظ�
	 */
	public boolean check(User user);
}
