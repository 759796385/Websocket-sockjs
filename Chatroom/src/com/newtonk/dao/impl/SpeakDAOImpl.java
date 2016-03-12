package com.newtonk.dao.impl;

import java.util.List;

import com.newtonk.dao.ISpeakDAO;
import com.newtonk.entity.Silence;
import com.newtonk.entity.User;

public class SpeakDAOImpl extends BaseDAOHibernate4<Silence> implements
		ISpeakDAO {

	/*
	 * true °üº¬
	 * 
	 * @see com.newtonk.dao.ISpeakDAO#containUser(com.newtonk.entity.User)
	 */
	@Override
	public Silence containUser(User user) throws Exception {
		String hql = "select s from Silence s left join s.user u where u.name=?0";
		List<Silence> result = getSessionFactory().getCurrentSession()
				.createQuery(hql).setCacheable(true)
				.setParameter("" + 0, user.getName()).list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
