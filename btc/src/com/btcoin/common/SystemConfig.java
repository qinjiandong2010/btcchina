package com.btcoin.common;

import org.apache.log4j.Logger;

import com.btcoin.utils.PropertiesUtil;

public class SystemConfig {

	private static final Logger log = Logger.getLogger(SystemConfig.class);
	/**
	 * 是否启用代理连接
	 */
	public static boolean ENABLED_PROXY = false;
	
	static {
		ENABLED_PROXY = PropertiesUtil.getPropertyBoolean(EnumConfig.FILE_SYSCONF_PROPERTIES.getName(),EnumConfig.enabled_proxy.getName(), false);
		log.debug("init SystemConfig finish.");
	}
}
