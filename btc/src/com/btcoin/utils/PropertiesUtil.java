package com.btcoin.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	private static Map<String,Properties> propertiesMap = new HashMap<String, Properties>();
	private static final Logger log = Logger.getLogger(PropertiesUtil.class);

	private static Properties getProperties(String fileName) {
		try {
			if( !propertiesMap.containsKey(fileName) ){
				Properties props = new Properties();
				InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
				props.load(in);
				propertiesMap.put(fileName, props);
			}
		} catch (Exception e) {
			log.error("Load properties error:",e);
		}
		return propertiesMap.get(fileName);
	}

	/**
	 * 获取key值
	 */
	public static String getProperty(String filename,String key,String defaultVulate) {
		Properties properties = getProperties(filename);
		String value = properties.getProperty(key);
		if(StringUtil.isNullOrEmpty(value)){
			value = defaultVulate;
		}
		return value;
	}
	/**
	 * 获取boolean值
	 * @param filename
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getPropertyBoolean(String filename,String key,boolean defaultValue){
		 try{
			 return Boolean.parseBoolean(getProperties(filename).get(key)+"");
		 }catch (Exception e) {
			return defaultValue;
		}
	}
	/**
	 * 获取int值
	 * @param filename
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getPropertyInt(String filename,String key,int defaultValue){
		 try{
			 return Integer.parseInt(getProperties(filename).get(key)+"");
		 }catch (Exception e) {
			return defaultValue;
		}
	}
}
