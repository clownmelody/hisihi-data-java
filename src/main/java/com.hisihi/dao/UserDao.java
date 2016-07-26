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
	@Autowired
	private  JdbcTemplate appJdbcTemplate;

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

	public int getAllChannelCount(){
		int count = jdbcTemplate.queryForInt("select count(*) from hisihi_data_channel");
		return count;
	}

	public int getPostCount(String mobile, String start_time, String end_time){
		String sql = "SELECT id FROM hisihi_ucenter_member WHERE mobile = '"+mobile+"'";
		int uid;
		try{
			uid = appJdbcTemplate.queryForObject(sql, java.lang.Integer.class);
		} catch (Exception e){
			return 0;
		}
		sql = "select count(*) from hisihi_forum_post " +
				"where uid="+uid+" and create_time between unix_timestamp('"+start_time+"')" +
				" and unix_timestamp('"+end_time+"')";
		int postCount = appJdbcTemplate.queryForInt(sql);
		return postCount;
	}

	public int getPostReplyCount(String mobile, String start_time, String end_time){
		String sql = "SELECT id FROM hisihi_ucenter_member WHERE mobile = '"+mobile+"'";
		int uid;
		try{
			uid = appJdbcTemplate.queryForObject(sql, java.lang.Integer.class);
		} catch (Exception e){
			return 0;
		}
		sql = "select count(*) from hisihi_forum_post_reply " +
				"where uid="+uid+" and create_time between unix_timestamp('"+start_time+"')" +
				" and unix_timestamp('"+end_time+"')";
		int postCount = appJdbcTemplate.queryForInt(sql);
		return postCount;
	}

}
