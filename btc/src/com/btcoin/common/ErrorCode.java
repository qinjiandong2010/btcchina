package com.btcoin.common;

public class ErrorCode {

	public static String DEFAULT_LANGUAGE= "en";
	
	public static int SUCCESS = 0;
	public static int FAILURE = 1;
	
	public static final int sys_err=2;
	/**
	 * redis 连接异常
	 */
	public static final int redis_conn_err=100;
	/**
	 * 用户名必须项
	 */
	public static final int un_require=101;
	/**
	 * 用户密码必须项
	 */
	public static final int pwd_require=102;
	/**
	 * 用户名必须项
	 */
	public static final int tradepwd_require=103;
	/**
	 * 订单类型必须项
	 */
	public static final int ordertype_require= 104;
	/**
	 * 购买异常
	 */
	public static final int buy_error=105;
	/**
	 * 会话超时
	 */
	public static final int session_timeout=106;
	/**
	 * 登录异常
	 */
	public static final int login_error=107;
	/**
	 * 卖出异常
	 */
	public static final int sell_error=108;
	/**
	 * 交易类型必须项
	 */
	public static final int tradetype_require=109;
	/**
	 * 价格必须项
	 */
	public static final int price_require=110;
	/**
	 * 数量必须项
	 */
	public static final int amount_require=110;
}
