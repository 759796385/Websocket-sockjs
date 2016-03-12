package com.newtonk.service;

import java.util.List;

import com.newtonk.entity.Silence;
import com.newtonk.entity.User;

public interface ISpeakService {
	public List<Silence> getAll() throws Exception;

	public void add(String userName) throws Exception;

	public void remove(Silence silence) throws Exception;

	public boolean canSpeak(User user) throws Exception;
}
