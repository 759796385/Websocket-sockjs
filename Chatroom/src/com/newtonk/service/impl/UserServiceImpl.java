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
	 * �û���¼
	 * 
	 * @see com.newtonk.service.IUserService#login(com.newtonk.entity.User)
	 */
	@Override
	public User login(User user) throws Exception {
		return dao.Login(user);
	}

	/*
	 * �û�ע�� false �û����ظ� true ע��ɹ�
	 * 
	 * @see com.newtonk.service.IUserService#regist(com.newtonk.entity.User)
	 */
	@Override
	public boolean regist(User user) throws Exception {
		// ����û����Ƿ��ظ�
		boolean result = dao.check(user);
		if (result) {
			return false;
		} else {// �û������ظ�����������
			dao.save(user);
			return true;
		}
	}

	/*
	 * ����û����Ƿ��ظ�
	 * 
	 * @see com.newtonk.service.IUserService#check(com.newtonk.entity.User)
	 */
	@Override
	public boolean check(User user) {
		return dao.check(user);
	}

}
