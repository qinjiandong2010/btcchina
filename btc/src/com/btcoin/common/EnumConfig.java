package com.btcoin.common;

public enum EnumConfig{
	
	FILE_SYSCONF_PROPERTIES("sysconf.properties"),
	
	enabled_proxy("enabled_proxy"),
	proxy_host("proxy_host"),
	proxy_port("proxy_port"),
	proxy_protocol("proxy_protocol"),
	
	redis_host("redis_host"),
	redis_port("redis_port");
	
	String name;
	
	private EnumConfig(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String toString(){
		return this.name;
	}
}
