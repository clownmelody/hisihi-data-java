package com.hisihi.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andyYang on 2015/8/20.
 */
public class UMClient {
    private final String url = "http://api.umeng.com";
    private final String email = "523453004@qq.com";
    private final String password = "heishehui123...";
    private final String authPath = "/authorize";  // 认证接口
    private final String appListPath = "/apps";  // 获取app列表
    private final String appCountPath = "/apps/count";  // 获取app数量
    private final String appBaseDataPath = "/apps/base_data";  // 获取apps基本数据
    private final String channelsPath = "/channels?appkey=";  // 获取所有渠道和数据
    private final String todayDataPath = "/today_data?appkey=";   // 获取今日数据
    private final String yesterdayDataPath = "/yesterday_data?appkey=";  // 获取昨日数据
    private final String newUserDataPath = "/new_users";  // 获取新增用户
    private final String activeUserDataPath = "/active_users";  // 获取活跃用户
    private final String durationDataPath = "/durations";  // 获取使用时长
    private final String retentionDataPath = "/retentions";  // 获取留存用户
    private Gson gson = new Gson();

    public String getAuthorize(){
        String auth = null;
        Map<String, String> data = new HashMap<String, String>();
        data.put("email", this.email);
        data.put("password", this.password);
        String content = gson.toJson(data);
        HttpResponseWrapper result = null;
        try {
            result = HttpClient.doPost(this.url+this.authPath, content);
            Map map = gson.fromJson(result.content, Map.class);
            auth = (String) map.get("auth_token");
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        return auth;
    }

    public String getAppsList(){
        String data = null;
        String auth = this.getAuthorize();
        HttpResponseWrapper result = null;
        try {
            String url = this.url+this.appListPath+"?page=1&per_page=10&auth_token="+auth;
            result = HttpClient.doGet(url);
            data = gson.toJson(result);
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        return data;
    }

    public String getAppsCount(){
        String data = null;
        String auth = this.getAuthorize();
        HttpResponseWrapper result = null;
        try {
            String url = this.url+this.appCountPath+"?auth_token="+auth;
            result = HttpClient.doGet(url);
            data = gson.toJson(result);
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        return data;
    }

    public String getAppsBaseData(){
        String data = null;
        String auth = this.getAuthorize();
        HttpResponseWrapper result = null;
        try {
            String url = this.url+this.appBaseDataPath+"?auth_token="+auth;
            result = HttpClient.doGet(url);
            data = result.content;
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args){
        UMClient client = new UMClient();
        System.out.println(client.getAppsBaseData());
    }

}
