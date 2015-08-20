package com.hisihi.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HiworksDao {
	private static Logger logger = LoggerFactory.getLogger(HiworksDao.class);
	@Autowired
	private JdbcTemplate appJdbcTemplate;

	public int getTotalDownloadCount() {
		int count = appJdbcTemplate.queryForInt("select sum(download) from hisihi_document_download");
		return count;
	}

}
