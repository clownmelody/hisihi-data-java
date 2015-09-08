package com.hisihi.utils;

import com.google.gson.Gson;
import com.hisihi.model.ActiveUserModel;
import com.sun.javafx.binding.StringFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andyYang on 2015/8/20.
 */
public class UMClient {
    private final String url = "http://api.umeng.com";
    private final String email = "523453004@qq.com";
    private final String password = "heishehui123...";
    private final String authPath = "/authorize";  // ��֤�ӿ�
    private final String appListPath = "/apps";  // ��ȡapp�б�
    private final String appCountPath = "/apps/count";  // ��ȡapp����
    private final String appBaseDataPath = "/apps/base_data";  // ��ȡapps��������
    private final String channelsPath = "/channels?appkey=";  // ��ȡ��������������
    private final String todayDataPath = "/today_data?appkey=";   // ��ȡ��������
    private final String yesterdayDataPath = "/yesterday_data?appkey=";  // ��ȡ��������
    private final String newUserDataPath = "/new_users";  // ��ȡ�����û�
    private final String activeUserDataPath = "/active_users";  // ��ȡ��Ծ�û�
    private final String durationDataPath = "/durations";  // ��ȡʹ��ʱ��
    private final String retentionDataPath = "/retentions";  // ��ȡ�����û�
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

    public String getActiveUser(String appkey, String start_date, String end_date, String period_type){
        String data = null;
        String auth = this.getAuthorize();
        HttpResponseWrapper result = null;
        try {
            String args = String.format("?auth_token=%s&appkey=%s&start_date=%s&end_date=%s&period=%s",
                    auth, appkey, start_date, end_date, period_type);
            String url = this.url+this.activeUserDataPath+args;
            result = HttpClient.doGet(url);
            data = result.content;
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        return data;
    }

    public String getRetainUser(String appkey, String start_date, String end_date, String period_type){
        String data = null;
        String auth = this.getAuthorize();
        HttpResponseWrapper result = null;
        try {
            String args = String.format("?auth_token=%s&appkey=%s&start_date=%s&end_date=%s&period=%s",
                    auth, appkey, start_date, end_date, period_type);
            String url = this.url+this.retentionDataPath+args;
            result = HttpClient.doGet(url);
            data = result.content;
        } catch (java.lang.Exception e1) {
            e1.printStackTrace();
        }
        return data;
    }

    public List getActiveUserData(String start_date, String end_date, String period_type){
        List<Map> list = new ArrayList<Map>();
        String anroid_appkey = Config.getConfig().getProperty("android_appkey");
        String ios_appkey = Config.getConfig().getProperty("ios_appkey");
        Gson gson = new Gson();
        ActiveUserModel activeUserModel = gson.fromJson(this.getActiveUser(anroid_appkey, start_date, end_date, period_type), ActiveUserModel.class);
        int length = activeUserModel.getDates().size();
        for(int i=0; i<length; i++){
            Map map = new HashMap();
            map.put("day", activeUserModel.getDates().get(i));
            map.put("android_active", activeUserModel.getData().getAll().get(i));
            list.add(map);
        }
        activeUserModel = gson.fromJson(this.getActiveUser(ios_appkey, start_date, end_date, period_type), ActiveUserModel.class);
        int index = 0;
        for (Map map: list){
            map.put("ios_active", activeUserModel.getData().getAll().get(index));
            index++;
        }
        return list;
    }

    public String getRetainUserData(String start_date, String end_date, String period_type, String device){
        String anroid_appkey = Config.getConfig().getProperty("android_appkey");
        String ios_appkey = Config.getConfig().getProperty("ios_appkey");
        String result = null;
        if("Android".equals(device)){
            result = this.getRetainUser(anroid_appkey, start_date, end_date, period_type);
        } else {
            result = this.getRetainUser(ios_appkey, start_date, end_date, period_type);
        }
        return result;
    }

    public static void main(String[] args) {
        UMClient client = new UMClient();
//        System.out.println(client.getAppsBaseData());
//        System.out.println(client.getAppsList());
//        System.out.print(new Gson().toJson(client.getActiveUserData(null, null, null)));
        client.getRetainUserData("2015-08-08", "2015-08-23", "daily", "android");
    }

}
