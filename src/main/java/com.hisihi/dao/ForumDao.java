package com.hisihi.dao;

import com.hisihi.model.ResolvedQuestionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class ForumDao {
	private static Logger logger = LoggerFactory.getLogger(ForumDao.class);
	@Autowired
	private JdbcTemplate appJdbcTemplate;

	public List getEverydayPostCount() {
		ResolvedQuestionBean model = new ResolvedQuestionBean();
		List list =  appJdbcTemplate.queryForList("select count(*) as count, FROM_UNIXTIME(create_time, '%Y%m%d') as date from hisihi_forum_post group by FROM_UNIXTIME(create_time, '%Y%m%d' )");
		return list;
	}

}
