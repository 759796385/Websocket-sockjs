package com.newtonk.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;

import com.newtonk.dao.ISpeakDAO;
import com.newtonk.dao.IUserDAO;
import com.newtonk.entity.Silence;
import com.newtonk.entity.User;
import com.newtonk.service.ISpeakService;

public class SpeakServiceImpl implements ISpeakService {
	private ISpeakDAO dao;
	private IUserDAO userDao;
	private static boolean flag = false;// ��������ˢ�±�־λ

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

	/*
	 * ��ӽ���
	 * 
	 * @see com.newtonk.service.ISpeakService#add(java.lang.String)
	 */
	@Override
	public void add(String userName) throws Exception {
		// System.out.println("----------------��ʼ����---------------");
		User user = userDao.getUserByName(new User(userName, null, null));
		if (user != null) {
			// writes items to the second-level cache. It does not read from the
			// second-level cache.
			// System.out.println("�����������������");
			dao.getSessionFactory().getCurrentSession()
					.setCacheMode(CacheMode.PUT);
			Silence s = new Silence(user);
			dao.save(s);
			flag = true;
			// �������ղ�ѯ���棬���¶�ȡ���ݽ��л���
			// System.out
			// .println("----------------------������ɣ����ˢ��------------------");
		}
	}

	/*
	 * �������
	 * 
	 * @see com.newtonk.service.ISpeakService#remove(com.newtonk.entity.Silence)
	 */
	@Override
	public void remove(Silence silence) throws Exception {
		// System.out.println("------------��ʼ�Ƴ�-------------");
		Silence s = dao.get(Silence.class, silence.getSid());
		if (s != null) {
			dao.delete(s);
			flag = true;
			// ɾ����Ҳ��������ղ�ѯ���棬�������ж�ȡ���ݻ���
			// System.out.println("-------------�Ƴ���ɣ���ǻ���ˢ��--------------");
		}
	}

	@Override
	public boolean canSpeak(User user) throws Exception {
		// true ������
		// return !dao.containUser(user);
		if (flag == true) {
			// System.out.println("��ն�������");
			dao.clearSecondCache();
			flag = false;
		}
		// System.out.println("--------------���˵��ǰ-------------");
		Silence result = dao.containUser(user);
		// ����--��һ�ζ��⻺�棬����������-ˢ�»���
		// δ������--��һ�ζ��⣬֮�������
		// System.out.println("-------------����Ƿ��ܹ�˵��------:" + result);
		return result == null;
	}

	/**
	 * ʵ�建��
	 */
	public void entityHuan() {
		Map cacheEntries = dao.getSessionFactory().getStatistics()
				.getSecondLevelCacheStatistics("com.newtonk.entity.Silence")
				.getEntries();
		// Map cacheUser = dao.getSessionFactory().getStatistics()
		// .getSecondLevelCacheStatistics("com.newtonk.entity.User")
		// .getEntries();
		System.out.println("ʵ�建��Silence:" + cacheEntries);
	}

	/**
	 * ��ѯ����
	 */
	public void findHuan(SessionFactory sf) {
		Statistics stats = sf.getStatistics();
		for (String query : stats.getQueries()) {
			QueryStatistics querycache = stats.getQueryStatistics(query);
			System.out.println(query + "��ѯ����" + querycache);

		}
	}
}
