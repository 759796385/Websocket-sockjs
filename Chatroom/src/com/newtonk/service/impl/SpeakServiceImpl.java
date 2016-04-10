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
	private static boolean flag = false;// 二级缓存刷新标志位

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
	 * 添加禁言
	 * 
	 * @see com.newtonk.service.ISpeakService#add(java.lang.String)
	 */
	@Override
	public void add(String userName) throws Exception {
		// System.out.println("----------------开始新增---------------");
		User user = userDao.getUserByName(new User(userName, null, null));
		if (user != null) {
			// writes items to the second-level cache. It does not read from the
			// second-level cache.
			// System.out.println("向二级缓存新增数据");
			dao.getSessionFactory().getCurrentSession()
					.setCacheMode(CacheMode.PUT);
			Silence s = new Silence(user);
			dao.save(s);
			flag = true;
			// 插入后，清空查询缓存，重新读取数据进行缓存
			// System.out
			// .println("----------------------新增完成，标记刷新------------------");
		}
	}

	/*
	 * 解除禁言
	 * 
	 * @see com.newtonk.service.ISpeakService#remove(com.newtonk.entity.Silence)
	 */
	@Override
	public void remove(Silence silence) throws Exception {
		// System.out.println("------------开始移除-------------");
		Silence s = dao.get(Silence.class, silence.getSid());
		if (s != null) {
			dao.delete(s);
			flag = true;
			// 删除后，也是重新清空查询缓存，从数据中读取数据缓存
			// System.out.println("-------------移除完成，标记缓存刷新--------------");
		}
	}

	@Override
	public boolean canSpeak(User user) throws Exception {
		// true 不包含
		// return !dao.containUser(user);
		if (flag == true) {
			// System.out.println("清空二级缓存");
			dao.clearSecondCache();
			flag = false;
		}
		// System.out.println("--------------检查说话前-------------");
		Silence result = dao.containUser(user);
		// 禁言--第一次读库缓存，当新增禁言-刷新缓存
		// 未被禁言--第一次读库，之后读缓存
		// System.out.println("-------------检查是否能够说话------:" + result);
		return result == null;
	}

	/**
	 * 实体缓存
	 */
	public void entityHuan() {
		Map cacheEntries = dao.getSessionFactory().getStatistics()
				.getSecondLevelCacheStatistics("com.newtonk.entity.Silence")
				.getEntries();
		// Map cacheUser = dao.getSessionFactory().getStatistics()
		// .getSecondLevelCacheStatistics("com.newtonk.entity.User")
		// .getEntries();
		System.out.println("实体缓存Silence:" + cacheEntries);
	}

	/**
	 * 查询缓存
	 */
	public void findHuan(SessionFactory sf) {
		Statistics stats = sf.getStatistics();
		for (String query : stats.getQueries()) {
			QueryStatistics querycache = stats.getQueryStatistics(query);
			System.out.println(query + "查询缓存" + querycache);

		}
	}
}
