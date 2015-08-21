package com.hisihi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	public static Properties getConfig(){
		Properties p = new Properties();
		try {
			InputStream in = Config.class.getResourceAsStream("/config.properties");
			//InputStream in = new FileInputStream(path);
			p.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static Properties getDBConfig(){
		Properties p = new Properties();
		String url = Config.class.getResource("").getPath().replaceAll("%20", " ");  
        String path = url.substring(0, url.indexOf("WEB-INF")) + "WEB-INF/database.properties";  
		try {
			InputStream in = new FileInputStream(path);
			p.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static Properties getDBIndexConfig(){
		Properties p = new Properties();
		String url = Config.class.getResource("").getPath().replaceAll("%20", " ");  
        String path = url.substring(0, url.indexOf("WEB-INF")) + "WEB-INF/index.properties";  
		try {
			InputStream in = new FileInputStream(path);
			p.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	public static void main(String[] ar){
		String str = Config.getConfig().getProperty("android_appkey");
		System.out.println("a_appkey:"+str);
	}
}
