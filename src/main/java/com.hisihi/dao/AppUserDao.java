package com.hisihi.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserDao {
	private static Logger logger = LoggerFactory.getLogger(AppUserDao.class);
	@Autowired
	private JdbcTemplate appJdbcTemplate;

	public int getTotalCount() {
		int count = appJdbcTemplate.queryForInt("select count(*) from hisihi_member");
		return count;
	}

	public int getStudentsCount(){
		int count = appJdbcTemplate.queryForInt("select count(*) from hisihi_auth_group_access where group_id=5");
		return count;
	}

	public int getTeachersCount(){
		int count = appJdbcTemplate.queryForInt("select count(*) from hisihi_auth_group_access where group_id=6");
		return count;
	}

}
