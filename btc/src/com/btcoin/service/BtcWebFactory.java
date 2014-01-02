package com.btcoin.service;

import org.apache.commons.lang.NotImplementedException;

import com.btcoin.service.impl.BtcchinaWeb;
import com.btcoin.service.impl.ChbtcWeb;
import com.btcoin.service.impl.OkcoinWeb;

/**
 * btc Web 接口实例工厂
 * @author Administrator
 *
 */
public class BtcWebFactory {

	/**
	 * 创建btc Web接口实例
	 * @param name
	 * @return
	 */
	public static AbstractBtcWeb getInstance(String market){
		if( market.equals(BtcchinaWeb.WEB_SERVICE_NAME)){
			return new BtcchinaWeb();
		}else if(market.equals(OkcoinWeb.WEB_SERVICE_NAME)){
			return new OkcoinWeb();
		}else if(market.equals(OkcoinWeb.WEB_SERVICE_NAME)){
			return new ChbtcWeb();
		}else{
			
			throw new NotImplementedException("未实现的服务.");
		}
	}
}
