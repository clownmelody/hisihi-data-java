package com.hisihi.service;

import com.hisihi.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	public boolean isUserCorrect(String username, String password){
		int count = userDao.isUserCorrect(username, password);
		if (count!=0) {
			return true;
		}
		return false;
	}

	public boolean addUser(String username, String password){
		int result = userDao.addUser(username, password);
		if (result!=0) {
			return true;
		}
		return false;
	}

	public int getChannelCount(String channel){
		return userDao.getChannelCount(channel);
	}

	public int getAllChannelCount(){
		return userDao.getAllChannelCount();
	}

	public int getPostCount(String mobile, String start_time, String end_time){
		int count = userDao.getPostCount(mobile, start_time, end_time);
		return count;
	}

	public int getPostReplyCount(String mobile, String start_time, String end_time){
		int count = userDao.getPostReplyCount(mobile, start_time, end_time);
		return count;
	}

}
