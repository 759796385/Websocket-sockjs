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
		dao.update(user);
		return true;
	}

	/**
	 * ����ܷ�ע�� user(name,email) �û������� -��ע�� �û��Ѵ���&δ����&���䱣��ʱ���ѹ���-��ע��
	 * 
	 * @see com.newtonk.service.IUserService#check(com.newtonk.entity.User)
	 */
	@Override
	public boolean checkCanRegist(User user) {
		// ��ȡ�û�
		User result = dao.getUserByName(user);
		if (result != null) {// �û�����
			if (result.getState()) {// �Ѽ���
				return false;
			} else {// δ����
				Date expireTime = result.getDate();
				return new Date().after(expireTime);// �����ʼ�����ʱ����ע��
			}
		} else {// �û�������
			return true;
		}
	}

	/**
	 * һСʱ��ʱ��
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
	 * ���ͱ���δ�����û� user(name,email) �û����м�¼��δ����������䱣��ʱ�� �û�������-���ñ���ʱ��
	 */
	@Override
	public int saveUnActivation(User user) {
		User result = dao.getUserByName(user);// �Ƿ���δ�����û�
		if (result != null) {// �����û�
			result.setDate(getAnHourTime());// һСʱ�����
			result.setState(false);
			return result.getUid();
		} else {// �����û�
			user.setDate(getAnHourTime());
			user.setState(false);
			return (int) dao.save(user);
		}
	}

	/*
	 * �����û�
	 * 
	 * @see com.newtonk.service.IUserService#getUserById(int)
	 */
	@Override
	public User getUserById(int encodeId) {
		return dao.get(User.class, encodeId);
	}
}
