package com.hisihi.dao;

/**
 * Created by shaolei on 2015/9/8 0008.
 */

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
import  java.util.HashMap;
import java.util.Map;

@Repository
public class AsoDao {
    private static Logger logger = LoggerFactory.getLogger(AsoDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存试玩ASO点击时传入的信息
     * @param appid     推广的广告ID      由广告主指定
     * @param ip        ip地址     渠道提供
     * @param mac       设备MAC地址  渠道提供
     * @param idfa      Idfa为源格式  渠道提供
     * @param callback  渠道回调地址  渠道提供
     * @param source    由广告主指定
     * @return
     */
    public int saveAsoInfo(String appid, String ip, String mac, String idfa,String callback,String source){
        int status = 0;
        return jdbcTemplate.update(
                "insert into hisihi_data_aso(appid,ip,mac,idfa,callback,source,status) values(?,?,?,?,?,?,?)",
                new Object[]{appid,ip,mac,idfa,callback,source,status},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.INTEGER}
        );
    }

    /**
     * 获取callback链接
     * @param idfa  Idfa为源格式  渠道提供
     * @return
     */
    public List getCallbackByIDFA(String idfa){
        List list = jdbcTemplate.queryForList("select id, callback from hisihi_data_aso where idfa ='" + idfa + "' order by id asc");
        return list;
    }

    /**
     *判断IDFA是否下载过
     * @param appid
     * @param idfa
     * @return
     */
    public Map isIDFADownload(String appid, String idfa){
        String[] idfas = idfa.split(",");
        Map dataMap = new HashMap();
        for(int i = 0; i < idfas.length; i++){
            int count = jdbcTemplate.queryForInt("select count(*) from hisihi_data_aso where appid='" + appid
                    + "' and idfa='" + idfas[i] + "' and status=1" );
            if(count > 0){
                dataMap.put(idfas[i],"1");
            }else{
                dataMap.put(idfas[i],"0");
            }
        }
        return dataMap;
    }

    /**
     * 若注册时找到了idfa则更新aso状态
     * @param id
     * @return
     */
    public int updateAsoStatus(int id){
        int status = 1;
        return jdbcTemplate.update(
                "update hisihi_data_aso set status = ? where id = ?",
                new Object[]{status,id},
                new int[]{java.sql.Types.INTEGER, java.sql.Types.INTEGER}
        );
    }
}
