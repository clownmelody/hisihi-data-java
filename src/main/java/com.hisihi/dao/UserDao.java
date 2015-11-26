package com.hisihi.dao;

import com.hisihi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	private static Logger logger = LoggerFactory.getLogger(UserDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int isUserCorrect(String username, String password){
		password = StringUtils.toMD5(password);
		int count = jdbcTemplate.queryForInt("select count(*) from hisihi_data_user where name=? and password=?",
				new Object[]{username, password});
		return count;
	}

	public int addUser(String username, String password){
		password = StringUtils.toMD5(password);
		return jdbcTemplate.update(
				"insert into hisihi_data_user(name, password) values(?,?)",
				new Object[]{username, password},
				new int[]{java.sql.Types.VARCHAR,java.sql.Types.VARCHAR}
		);
	}

	public int getChannelCount(String channel){
		int count = jdbcTemplate.queryForInt("select count(*) from hisihi_data_channel where channel=?",
				new Object[]{channel});
		return count;
	}

}
