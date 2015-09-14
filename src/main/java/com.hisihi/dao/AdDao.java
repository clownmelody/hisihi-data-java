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

import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        int status = 0;
        return jdbcTemplate.update(
                "insert into hisihi_data_qumi(appid, channel,mac,idfa,callback,status) values(?,?,?,?,?,?)",
                new Object[]{appid, channel,mac,idfa,callback,status},
                new int[]{java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR, Types.INTEGER}
        );
    }

    /**
     * 获取callback链接
     * @param idfa  Idfa为源格式  渠道提供
     * @return
     */
    public List getCallbackByIDFA(String idfa){
        List list = jdbcTemplate.queryForList("select id, callback from hisihi_data_qumi where idfa ='" + idfa + "' order by id asc");
        return list;
    }

    /**
     * 若注册时找到了idfa则更新广告状态
     * @param id
     * @return
     */
    public int updateAdStatus(int id){
        int status = 1;
        return jdbcTemplate.update(
                "update hisihi_data_qumi set status = ? where id = ?",
                new Object[]{status,id},
                new int[]{java.sql.Types.INTEGER, java.sql.Types.INTEGER}
        );
    }

    /**
     *记录渠道和设备iemi信息
     * @param channel
     * @param imei
     * @return
     */
    public int recordChannel(String channel, String imei){
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = "";
        nowTime = df.format(dt);
        Timestamp recordTime = Timestamp.valueOf(nowTime);
        return jdbcTemplate.update(
                "insert into  hisihi_data_channel (channel, imei, recordtime) values (?,?,?)",
                new Object[]{channel,imei,recordTime},
                new int[]{Types.VARCHAR, Types.VARCHAR,Types.TIMESTAMP}
        );
    }

    /**
     * iemi设备是否已经存在
     * @param imei
     * @return
     */
    public boolean isIEMIExist(String imei){
        int count = jdbcTemplate.queryForInt("select count(*) from hisihi_data_channel where imei = '" + imei + "'");
        if(count > 0) {
            return true;
        }else {
            return false;
        }
    }
}
