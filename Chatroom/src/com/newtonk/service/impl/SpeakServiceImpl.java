package com.newtonk.service.impl;

import java.util.List;

import org.hibernate.Hibernate;

import com.newtonk.dao.ISpeakDAO;
import com.newtonk.dao.IUserDAO;
import com.newtonk.entity.Silence;
import com.newtonk.entity.User;
import com.newtonk.service.ISpeakService;

public class SpeakServiceImpl implements ISpeakService {
	private ISpeakDAO dao;
	private IUserDAO userDao;

	public void setDao(ISpeakDAO dao) {
		this.dao = dao;
	}

	public void setUserDao(IUserDAO userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<Silence> getAll() throws Exception {
		List<Silence> result = dao.findAll(Silence.class);
		for (Silence silence : result) {
			Hibernate.initialize(silence.getUser());
		}
		return result;
	}

	@Override
	public void add(String userName) throws Exception {
		User user = userDao.getUserByName(new User(userName, null, null));
		if (user != null) {
			Silence s = new Silence(user);
			dao.save(s);
		}
	}

	@Override
	public void remove(Silence silence) throws Exception {
		Silence s = dao.get(Silence.class, silence.getSid());
		if (s != null) {
			dao.delete(s);
		}
	}

	@Override
	public boolean canSpeak(User user) throws Exception {
		// true ²»°üº¬
		// return !dao.containUser(user);
		Silence result = dao.containUser(user);
		return result == null;
	}
}
