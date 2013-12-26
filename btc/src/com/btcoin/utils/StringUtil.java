package com.btcoin.utils;


import java.io.UnsupportedEncodingException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 转换字符串的编码
 * @author Simon
 */

public class StringUtil {
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁�?*/
	public static final String US_ASCII = "US-ASCII";

	/** ISO拉丁字母�?No.1，也叫做ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/** 8 �?UCS 转换格式 */
	public static final String UTF_8 = "UTF-8";

	/** 16 �?UCS 转换格式，Big Endian(�?��地址存放高位字节）字节顺�?*/
	public static final String UTF_16BE = "UTF-16BE";

	/** 16 �?UCS 转换格式，Litter Endian（最高地�?��放地位字节）字节顺序 */
	public static final String UTF_16LE = "UTF-16LE";

	/** 16 �?UCS 转换格式，字节顺序由可�?的字节顺序标记来标识 */
	public static final String UTF_16 = "UTF-16";

	/** 中文超大字符�?**/
	public static final String GBK = "GBK";

	public static final String GB2312 = "GB2312";

	/** 将字符编码转换成US-ASCII�?*/
	public static String toASCII(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, US_ASCII);
	}

	/** 将字符编码转换成ISO-8859-1 */
	public static String toISO_8859_1(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, ISO_8859_1);
	}

	/** 将字符编码转换成UTF-8 */
	public static String toUTF_8(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_8);
	}

	/** 将字符编码转换成UTF-16BE */
	public static String toUTF_16BE(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16BE);
	}

	/** 将字符编码转换成UTF-16LE */
	public static String toUTF_16LE(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16LE);
	}

	/** 将字符编码转换成UTF-16 */
	public static String toUTF_16(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16);
	}

	/** 将字符编码转换成GBK */
	public static String toGBK(String str) throws UnsupportedEncodingException {
		return changeCharset(str, GBK);
	}

	/** 将字符编码转换成GB2312 */
	public static String toGB2312(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, GB2312);
	}

	/**
	 * 字符串编码转换的实现方法
	 * @param str
	 *            待转换的字符�?
	 * @param newCharset
	 *            目标编码
	 */
	public static String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// 用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
			byte[] bs = str.getBytes();
			return new String(bs, newCharset); // 用新的字符编码生成字符串
		}
		return null;
	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            待转换的字符�?
	 * @param oldCharset
	 *            源字符集
	 * @param newCharset
	 *            目标字符�?
	 */
	public static String changeCharset(String str, String oldCharset,
			String newCharset) throws UnsupportedEncodingException {
		if (str != null) {
			// 用源字符编码解码字符�?
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}
	
	public static boolean isEmpty(String value){
		if(value != null && !"".equals(value.trim()) && !"null".equals(value))
			return false;
		return true;
	}
	
	public static String stringDate(Date now) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(now);
	}
	
	/**
	 * 判读字符串是否为空
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(String value){
		if(value==null)
			return true;
		return value.trim().length()==0;
	}
	
	/**
	 * 判读字符串是否为空
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(Object value){
		if(value==null)
			return true;
		return value.toString().trim().length()==0;
	}
	
	/**
	 * 判断是否能转换为正整数
	 * @param value
	 * @return
	 */
	public static boolean isNum(String value){              
		String checkPattern = "^\\d+$";
		return value.matches(checkPattern);
	}
	
	/**
	 * 判断是否能转换为正整数
	 * @param value
	 * @return
	 */
	public static boolean isNum(Object value){
		String checkPattern = "^\\d+$";
		return value.toString().matches(checkPattern);
	}
	
	/**
	 * 验证JSON格式是否正确
	 * @param param
	 * @return
	 */
	public static boolean isJSON(String param) {
		try {
			JSONObject jo = JSONObject.fromObject(param);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * 检查JSON元素是否存在或为空
	 * 
	 */
	public static boolean isJSONObjectOk(JSONObject jso,String key){
		if( jso != null && jso.containsKey(key) && jso.get(key)!=null && !"".equals(jso.get(key)))
			return true;
		else
			return false;
	}
	
	
	/**
	 * 检查JSON元素是否存在或为空
	 * 
	 */
	public static boolean isJSONArrayOk(JSONObject jso,String key){
		if(jso.containsKey(key)){
			Object o=jso.get(key);
			if(o instanceof JSONArray ){
				if(jso.getJSONArray(key)!=null && jso.getJSONArray(key).size()>0){
					return true;
				}
			}
				
			return false;
		}else
			return false;
	}
	
	/**
	 * 检查JSON元素是否是长整形数
	 * 
	 */
	public static boolean isJSONObjectIsLong(JSONObject jso,String key){
		if(jso.containsKey(key) && jso.get(key)!=null && !"".equals(jso.getString(key)))		
			try{
				jso.getLong(key);
				return true;
			}catch(Exception e){
				return false;
			}
		else
			return false;
	}
	
	/**
	 * 检查JSON元素是否是整数
	 * 
	 */
	public static boolean isJSONObjectIsInt(JSONObject jso,String key){
		if(jso.containsKey(key) && jso.get(key)!=null && !"".equals(jso.getString(key)))		
			try{
				jso.getInt(key);
				return true;
			}catch(Exception e){
				return false;
			}
		else
			return false;
	}
	
	
	/**
	 * 检查JSON元素是否是浮点数
	 * 
	 */
	public static boolean isJSONObjectIsDouble(JSONObject jso,String key){
		if(jso.containsKey(key) && jso.get(key)!=null && !"".equals(jso.getString(key)))		
			try{
				jso.getInt(key);
				return true;
			}catch(Exception e){
				return false;
			}
		else
			return false;
	}
	
	
	public static String getCustomerID(long uid){
		String customerid="C";
		String uidstr=String.valueOf(uid);
		while(uidstr.length()<8){
			uidstr="0"+uidstr;
		}
		customerid+=uidstr;
		return customerid;
	}
	
	
	/**
	 * 验证email格式是否正确
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String checkPattern = "^([a-z0-9A-Z]+[-|._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.){1,2}[a-zA-Z]{2,3}$";
		return email.matches(checkPattern);
	}
	
	/**
	 * 验证email格式是否正确
	 * @param email
	 * @return
	 */
	public static boolean isEmail(Object email) {
		String checkPattern = "^([a-z0-9A-Z]+[-|._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.){1,2}[a-zA-Z]{2,3}$";
		return email.toString().matches(checkPattern);
	}
	 /**
	 * 检查JSON元素是否存在或为空
	 * 
	 */
	public static boolean isMapOk(Map jso,String key){
		if(jso.containsKey(key) && jso.get(key)!=null && !"".equals(jso.get(key)))
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
//		System.out.println(StringUtil.isNum("11"));//120|((1[0-1]|\d)?\d)
//		JSONObject jso=new JSONObject();
//		jso.put("test", "");
//		System.out.println(StringUtil.isJSONObjectOk(jso, "test"));
		System.out.println(StringUtil.getCustomerID(12l));
	}
}