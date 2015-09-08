package com.hisihi.dao;

/**
 * Created by shaolei on 2015/9/8 0008.
 */

import com.hisihi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdDao {
    private static Logger logger = LoggerFactory.getLogger(AdDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存趣米广告接入信息
     * @param appid     推广的广告ID      由广告主指定
     * @param channel   渠道方唯一标识     由广告主指定
     * @param mac       设备MAC地址  渠道提供
     * @param idfa      Idfa为源格式  渠道提供
     * @param callback  渠道回调地址  渠道提供
     * @return
     */
    public int saveAdInfo(String appid, String channel, String mac, String idfa,String callback){

        return jdbcTemplate.update(
                "insert into hisihi_data_qumi(appid, channel,mac,idfa,callback) values(?,?,?,?,?)",
                new Object[]{appid, channel,mac,idfa,callback},
                new int[]{java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR}
        );
    }

}
