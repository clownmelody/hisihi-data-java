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
     * ����Ȥ�׹�������Ϣ
     * @param appid     �ƹ�Ĺ��ID      �ɹ����ָ��
     * @param channel   ������Ψһ��ʶ     �ɹ����ָ��
     * @param mac       �豸MAC��ַ  �����ṩ
     * @param idfa      IdfaΪԴ��ʽ  �����ṩ
     * @param callback  �����ص���ַ  �����ṩ
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
