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
	 * ����Ƿ���ע��
	 * 
	 * @param user
	 * @return true�ظ� false���ظ�
	 */
	public boolean checkCanRegist(User user);

	/**
	 * ����δ�����û�
	 * 
	 * @param user
	 * @return
	 */
	public int saveUnActivation(User user);

	/**
	 * �����û�
	 * 
	 * @param encodeId
	 * @return
	 */
	public User getUserById(int encodeId);
}
