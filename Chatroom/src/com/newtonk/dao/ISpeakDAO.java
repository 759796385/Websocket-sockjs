package com.newtonk.dao;

import com.newtonk.entity.Silence;
import com.newtonk.entity.User;

public interface ISpeakDAO extends BaseDAO<Silence> {
	public Silence containUser(User user) throws Exception;

	public void clearSecondCache();
}
