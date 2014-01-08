package com.btcoin.factory;

import org.apache.commons.lang.NotImplementedException;

import com.btcoin.service.AbstractBTCWeb;
import com.btcoin.service.impl.BTCChinaWeb;
import com.btcoin.service.impl.CHBTCWeb;
import com.btcoin.service.impl.OKCoinWeb;

/**
 * btc Web 实例工厂
 * @author Administrator
 *
 */
public class BTCWebFactory {

	/**
	 * 创建btc Web实例
	 * @param name
	 * @return
	 */
	public static AbstractBTCWeb getInstance(String market){
		if( market.equals(BTCChinaWeb.WEB_SERVICE_NAME)){
			return new BTCChinaWeb();
		}else if(market.equals(OKCoinWeb.WEB_SERVICE_NAME)){
			return new OKCoinWeb();
		}else if(market.equals(CHBTCWeb.WEB_SERVICE_NAME)){
			return new CHBTCWeb();
		}else{
			throw new NotImplementedException("未实现的交易市场.");
		}
	}
}
