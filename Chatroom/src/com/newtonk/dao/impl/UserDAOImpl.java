package com.newtonk.dao.impl;

import java.util.List;

import com.newtonk.dao.IUserDAO;
import com.newtonk.entity.User;

public class UserDAOImpl extends BaseDAOHibernate4<User> implements IUserDAO {
	/**
	 * 登陆 返回查询到的用户
	 */
	@Override
	public User Login(User user) throws Exception {
		List<User> list = find("from User u where u.name=?0 and u.password=?1",
				user.getName(), user.getPassword());
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 检查用户名是否重复
	 * 
	 * @return true重复 false不重复
	 */
	@Override
	public boolean check(User user) {
		List<User> result = find("from User u where u.name=?0", user.getName());
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

}
