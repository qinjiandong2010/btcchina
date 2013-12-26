package com.btcoin.common;

public class ErrorCode {

	public static String DEFAULT_LANGUAGE= "en";
	
	public static String SUCCESS = "0";
	public static String FAILURE = "1";


	//redis 20000-30000
	public static final String redis_conn_err$20000="20000";
	
	//bitcchina 10000-20000
	/**
	 * 重新登录
	 */
	//buyOrder
	public static final String un_require$10000="10000";
	public static final String pwd_require$10001="10001";
	public static final String tradepwd_require$10002="10002";
	public static final String ordertype_require$10003="10003";
	public static final String buy_error$10004="10004";
	public static final String session_timeout$10005="10005";
	
	//login
	public static final String un_require$10100="10100";
	public static final String pwd_require$10101="10101";
	public static final String login_error$10103="10103";
	
	//sellOrder
	public static final String un_require$10200="10200";
	public static final String pwd_require$10201="10201";
	public static final String tradepwd_require$10202="10202";
	public static final String ordertype_require$10203="10203";
	public static final String sell_error$10204="10204";
	public static final String session_timeout$10205="10205";
	//getMarketDepth
	public static final String un_require$10300="10300";
	public static final String pwd_require$10301="10301";
	public static final String session_timeout$10302="10302";
	public static final String tradetype_require$10303="10303";
	
	
//	public static String DEFAULT_NOMONEY_TITLE_zh= "账户余额不足 ,请及时充值!";
//	public static String DEFAULT_NOMONEY_TITLE_en= "Account balance is insufficient, please recharge in time!";
//	
//	public static String DEFAULT_NOMONEY_MESSAGE_zh= "由于你的账户余额不足续费，为了不影响您正常使用，请及时充值!";
//	public static String DEFAULT_NOMONEY_MESSAGE_en= "Because of your account balance is insufficient renewals, in order not to affect your normal use, please recharge in time!";
//	
//	public static String USEROPERATORLOG_SUCCESS_zh= "系统自动销毁用户{0}的产品:{1},操作成功！";
//	public static String USEROPERATORLOG_SUCCESS_en= "The system automatically destroy the user {0} products: {1}, successful operation!";
//	
//	public static String USEROPERATORLOG_FAIL_zh= "系统自动销毁用户{0}的产品:{1},操作失败！";
//	public static String USEROPERATORLOG_FAIL_en= "The system automatically destroy the user {0} products: {1}, operation failed!";
//	
//	
//	public static String USEROPERATORLOG_FEE_SUCCESS_zh= "系统自动为用户{0}的产品:{1}续费,操作成功！";
//	public static String USEROPERATORLOG_FEE_SUCCESS_en= "The system automatically destroy the user {0} products: {1}, successful operation!";
//	
//	public static String USEROPERATORLOG_FEE_FAIL_zh= "系统自动为用户{0}的产品:{1}续费,操作失败！";
//	public static String USEROPERATORLOG_FEE_FAIL_en= "The system automatically destroy the user {0} products: {1}, operation failed!";
//	
//	
//	public static String CONTINUEFEE_DESP_zh= "产品续费，产品名:{0}";
//	public static String CONTINUEFEE_DESP_en= "The product renewal, product name: {0}";
//	
//	
//	
//	
//	public static String CONTINUEFEE_LOG_DESP_zh= "用户的产品:{0}续费!";
//	public static String CONTINUEFEE_LOG_DESP_en= "Users of the product: {0} renewals!";
//	
//	public static String CONTINUEFEE_ACCOUNT_LOG_DESP_zh= "用户给产品续费，续费订单号:{0}";
//	public static String CONTINUEFEE_ACCOUNT_LOG_DESP_en= "The user to the product renewal, renewal Order No: {0}";
//	
//	public static String CONTINUEFEE_ACCOUNT_LOG1_DESP_zh= "用户给产品续费，续费订单号:{0}";
//	public static String CONTINUEFEE_ACCOUNT_LOG1_DESP_en= "The user to the product renewal, renewal Order No: {0}";
//	
//	public static String TRADE_FAIL_DESP_zh= "产品交易失败，订单号{0}";
//	public static String TRADE_FAIL_DESP_en= "Transaction failure,order number:{0}";
//	
//	public static String TRADE_FAIL_DESP1_zh= "VFOS交付数据不合法，产品交易失败，订单号{0}";
//	public static String TRADE_FAIL_DESP1_en= "Transaction failure,order number:{0}";
//	
//	public static String TRADE_FAIL_DESP2_zh= "产品交付超时，产品交易失败，订单号{0}";
//	public static String TRADE_FAIL_DESP2_en= "Transaction failure,order number:{0}";
//	
//	
//	public static String TRADE_SUCCESS_DESP_zh= "产品交易成功，订单号{0}";
//	public static String TRADE_SUCCESS_DESP_en= "Transaction successful,order number:{0}";
//	
//	public static String SELL_OTDERL_DESP_zh= "销售订单，订单号：{0}";
//	public static String SELL_OTDERL_DESP_en= "Sales order, order no:{0}";
//	
//	public static String BUY_OTDERL_DESP_zh= "购买订单，订单号：{0}";
//	public static String BUY_OTDERL_DESP_en= "Purchase order, order no:{0}";
//	
//	public static String WARNING_TITLE_zh="账户余额不足 ,请及时充值!";
//	public static String WARNING_TITLE_en="Account balance is insufficient, please recharge in time!";
//	public static String WARNING_MESSAGE_zh="由于你的账户余额不足续费，为了不影响您正常使用，请及时充值!";
//	public static String WARNING_MESSAGE_en="Because of your account balance is insufficient renewals, in order not to affect your normal use, please recharge in time!";

	
	
	public static String WARNING_TITLE="title";
	public static String WARNING_MESSAGE="message";
	public static String RECHARGE_REMARK="recharge.remark";
	public static String PARENT_ACCOUNGLOG_REMARK="parent.account.remark";
	public static String ACCOUNGLOG_REMARK="parent.account.remark";
	public static String OPERATLOG_REMARK="operatlog.remark";
	
	
//	public static String ADDMONEY_DESP_zh= "zh";
//	public static String ADDMONEY_DESP_en= "zh";
//	public static String GIVEMONEY_DESP_zh= "zh";
//	public static String GIVEMONEY_DESP_en= "zh";
	
//	public static String SUCCESS = "0";
//	public static String SUCCESS_SUMARY = "操作成功！";
//	public static String FAILURE = "1";
//	public static String FAILURE_SUMARY = "操作失败！";
//	public static String DATA_ERROR = "2";
//	public static String DATA_ERROR_SUMARY = "数据格式不正确";
//
//	public static String NO_USER_ERROR = "201";
//	public static String NO_USER_ERROR_SUMARY = "用户非法";
//	
//	public static String NO_USER_ERROR_202 = "202";
//	public static String NO_USER_ERROR_202_SUMARY = "用户非本卖场用户，不能登录!";
//
//	public static String NO_USER_ERROR_203 = "203";
//	public static String NO_USER_ERROR_203_SUMARY = "用户名不存在或密码错误!";
//	
//	public static String NO_USER_ERROR_204 = "204";
//	public static String NO_USER_ERROR_204_SUMARY = "您的帐户未激活，请前往您的邮箱激活!";
//	
//	public static String NO_USER_ERROR_205 = "205";
//	public static String NO_USER_ERROR_205_SUMARY = "您的帐户被停用，请联系客服!";
//	
//
//
//	public static String ORDER_NOTEXIST_6201 = "6201";
//	public static String ORDER_NOTEXIST_6201_DES = "定单不存在!";
//	
//	//14601-14699
//	public static String ORDER_NOTEXIST_14601 = "14601";
//	public static String ORDER_NOTEXIST_14601_DES = "校验码不能为空!";
//	public static String ORDER_NOTEXIST_14602 = "14602";
//	public static String ORDER_NOTEXIST_14602_DES = "用户名不能为空!";
//	
//    //订单接口错误码  
//	public static String ORDER_SUCCESS_DES = "下单成功!";
//	public static String ORDER_ERROR_6301 = "6301";
//	public static String ORDER_ERROR_6301_DES = "用户不存在！";
//	public static String ORDER_ERROR_6302 = "6302";
//	public static String ORDER_ERROR_6302_DES = "帐户余额不足！";
//	public static String ORDER_ERROR_6303 = "6303";
//	public static String ORDER_ERROR_6303_DES = "产品货源不足！";
//	public static String ORDER_ERROR_6304 = "6304";
//	public static String ORDER_ERROR_6304_DES = "其他异常！";
//	public static String ORDER_ERROR_6305 = "6305";
//	public static String ORDER_ERROR_6305_DES = "VFOS异常！";
//	public static String ORDER_ERROR_6306 = "6306";
//	public static String ORDER_ERROR_6306_DES = "总价计算有错误,与实际价格不一致！";
//	public static String ORDER_ERROR_6307 = "6307";
//	public static String ORDER_ERROR_6307_DES = "SOHOCloud产品不支持在AP处购买配件或附件!";
//	public static String ORDER_ERROR_6308 = "6308";
//	public static String ORDER_ERROR_6308_DES = "产品数量错误，必须是正整数!";
//	public static String ORDER_ERROR_6309 = "6309";
//	public static String ORDER_ERROR_6309_DES = "产品的购买周期不一致";
//	public static String ORDER_ERROR_6310 = "6310";
//	public static String ORDER_ERROR_6310_DES = "产品的价格不能为空!";
//	public static String ORDER_ERROR_6311 = "6311";
//	public static String ORDER_ERROR_6311_DES = "数据格式不合法!";
//	public static String ORDER_ERROR_6312 = "6312";
//	public static String ORDER_ERROR_6312_DES = "主产品已停用或销毁，不能为其增加配件或附件!";
//	public static String ORDER_ERROR_6313 = "6313";
//	public static String ORDER_ERROR_6313_DES = "上级产品已经下架,不能购买产品!";
//	public static String ORDER_ERROR_6314 = "6314";
//	public static String ORDER_ERROR_6314_DES = "上级用户已经停用";
//	public static String ORDER_ERROR_6315= "6315";
//	public static String ORDER_ERROR_6315_DES = "上级用户的信用已经透支完";
//	public static String ORDER_ERROR_6316= "6316";
//	public static String ORDER_ERROR_6316_DES = "产品价格有错误，上级产品存在价格过期或没定价";
//	//续费接口错误码
//	public static String CONTIUE_FEE_SUCCESS_DES = "续费成功!";
//	public static String CONTIUE_FEE_ERROR_11701 = "11701";
//	public static String CONTIUE_FEE_ERROR_11701_DES = "续费用户不存在！";
//	public static String CONTIUE_FEE_ERROR_11702 = "11702";
//	public static String CONTIUE_FEE_ERROR_11702_DES = "续费帐户余额不足！";
//
//	//支付充值
//	public static String ADDMONEY_ERROR_8201 = "8201";
//	public static String ADDMONEY_ERROR_8201_DES = "上级帐户余额不足！";
//	public static String ADDMONEY_ERROR_8202 = "8202";
//	public static String ADDMONEY_ERROR_8202_DES = "支付方式不合法！";
//	public static String ADDMONEY_ERROR_8203 = "8203";
//	public static String ADDMONEY_ERROR_8203_DES = "请输入充值金额";
//	//上级充值
//	public static String GIVEMONEY_ERROR_8301 = "8301";
//	public static String GIVEMONEY_ERROR_8301_DES = "帐户余额不足！";
//	public static String GIVEMONEY_ERROR_8302 = "8302";
//	public static String ADDMONEY_ERROR_8302_DES = "支付方式不合法！";
//
//	//查询产品定价列表
//	public static String QUERYPRODUCTPRICE_ERROR_4905 = "4905";
//	public static String QUERYPRODUCTPRICE_ERROR_4905_DES = "查询产品价格失败，没有产品价格信息！";
//	public static String QUERYPRODUCTPRICE_ERROR_4903 = "4903";
//	public static String QUERYPRODUCTPRICE_ERROR_4903_DES = "查询产品价格异常！";
//	
//
//	//注册接口错误码
//	public static String ORDER_ERROR_101 = "101";
//	public static String ORDER_ERROR_101_DES = "该邮箱已被占用！";
//	public static String ORDER_ERROR_102 = "102";
//	public static String ORDER_ERROR_102_DES = "参数错误！";
//	public static String ORDER_ERROR_103 = "103";
//	public static String ORDER_ERROR_103_DES = "原始密码输入不正确！";
//	public static String ORDER_ERROR_104 = "104";
//	public static String ORDER_ERROR_104_DES = "未找到该公司信息！";
//	public static String ORDER_ERROR_105 = "105";
//	public static String ORDER_ERROR_105_DES = "未找到该用户信息！";
//	public static String ORDER_ERROR_106 = "106";
//	public static String ORDER_ERROR_106_DES = "未找到该用户账户信息！";
//	public static String ORDER_ERROR_107 = "107";
//	public static String ORDER_ERROR_107_DES = "未找到该系统用户信息！";
//	public static String ORDER_ERROR_108 = "108";
//	public static String ORDER_ERROR_108_DES = "加密字符错误！";
//	public static String ORDER_ERROR_109 = "109";
//	public static String ORDER_ERROR_109_DES = "该配置记录不存在！";
//	public static String ORDER_ERROR_110 = "110";
//	public static String ORDER_ERROR_110_DES = "该用户已存在！";
//	public static String ORDER_ERROR_111 = "111";
//	public static String ORDER_ERROR_111_DES = "VFOS创建用户失败！";
//	public static String ORDER_ERROR_112 = "112";
//	public static String ORDER_ERROR_112_DES = "该银行账户已存在！";
//	public static String ORDER_ERROR_113 = "113";
//	public static String ORDER_ERROR_113_DES = "此充值方式已存在！";
//	public static String ORDER_ERROR_114 = "114";
//	public static String ORDER_ERROR_114_DES = "未找到该银行账户信息！";
//	public static String ORDER_ERROR_115 = "115";
//	public static String ORDER_ERROR_115_DES = "上级停用用户，不能自己激活！";
//	
//	public static String ORDER_ERROR_116= "116";
//	public static String ORDER_ERROR_116_DES = "通知VFOS失败，本此操作失败";
//
//	public static String USER_ERROR_202 = "202";
//	public static String USER_ERROR_501 = "501";
//	public static String USER_ERROR_501_DES = "email不能为空！";
//	public static String USER_ERROR_502 = "502";
//	public static String USER_ERROR_502_DES = "密码不能为空！";
//	public static String USER_ERROR_503 = "503";
//	public static String USER_ERROR_503_DES = "重复新密码不能为空！";
//	public static String USER_ERROR_504 = "504";
//	public static String USER_ERROR_504_DES = "用户昵称不能为空！";
//	public static String USER_ERROR_505 = "505";
//	public static String USER_ERROR_505_DES = "用户名不能为空！";
//	public static String USER_ERROR_506 = "506";
//	public static String USER_ERROR_506_DES = "新密码不能为空！";
//	public static String USER_ERROR_507 = "507";
//	public static String USER_ERROR_507_DES = "用户账号不能为空！";
//	public static String USER_ERROR_508 = "508";
//	public static String USER_ERROR_508_DES = "标题不能为空！";
//	public static String USER_ERROR_509 = "509";
//	public static String USER_ERROR_509_DES = "消息类型不能为空！";
//	public static String USER_ERROR_510 = "510";
//	public static String USER_ERROR_510_DES = "消息内容不能为空！";
//	public static String USER_ERROR_511 = "511";
//	public static String USER_ERROR_511_DES = "公司不能为空！";
//	public static String USER_ERROR_512 = "512";
//	public static String USER_ERROR_512_DES = "开户银行不能为空！";
//	public static String USER_ERROR_513 = "513";
//	public static String USER_ERROR_513_DES = "账户名不能为空！";
//	public static String USER_ERROR_514 = "514";
//	public static String USER_ERROR_514_DES = "银行账户不能为空！";
//	public static String USER_ERROR_515 = "515";
//	public static String USER_ERROR_515_DES = "状态不能为空！";
//	public static String USER_ERROR_516 = "516";
//	public static String USER_ERROR_516_DES = "充值配置为空或不正确！";
//	public static String USER_ERROR_517 = "517";
//	public static String USER_ERROR_517_DES = "等级名称不能为空！";
//	public static String USER_ERROR_518 = "518";
//	public static String USER_ERROR_518_DES = "公司名称不能为空！";
//	public static String USER_ERROR_519 = "519";
//	public static String USER_ERROR_519_DES = "公司logo不能为空！";
//	public static String USER_ERROR_520 = "520";
//	public static String USER_ERROR_520_DES = "公司简称不能为空！";
//	public static String USER_ERROR_521 = "521";
//	public static String USER_ERROR_521_DES = "公司电话不能为空！";
//	public static String USER_ERROR_522 = "522";
//	public static String USER_ERROR_522_DES = "客服电话不能为空！";
//	public static String USER_ERROR_523 = "523";
//	public static String USER_ERROR_523_DES = "公司地址不能为空！";
//	public static String USER_ERROR_524 = "524";
//	public static String USER_ERROR_524_DES = "公司网址不能为空！";
//
//	public static String USER_ERROR_601 = "601";
//	public static String USER_ERROR_601_DES = "email格式不正确！";
//	public static String USER_ERROR_602 = "602";
//	public static String USER_ERROR_602_DES = "两次输入密码不一致！";
//
//	//交易接口错误吗
//	public static String QUERY_ORSER_ERROR_11001 = "11001";
//	public static String QUERY_ORSER_ERROR_11001_DES = "订单交易中...";
//	public static String QUERY_ORSER_ERROR_11002 = "11002";
//	public static String QUERY_ORSER_ERROR_11002_DES = "订单交易失败...";
//	public static String QUERY_ORSER_ERROR_11003 = "11003";
//	public static String QUERY_ORSER_ERROR_11003_DES = "订单不存在";
//
//	// 7401—7499
//	public static String COMPLAINT_ERROR_7401 = "7401";
//	public static String COMPLAINT_ERROR_7401_DES = "未找到我的投诉状态(0/1)！"; 
//	public static String COMPLAINT_ERROR_7402 = "7402"; 
//	public static String COMPLAINT_ERROR_7402_DES = "未找到我的投诉信息！";
//	public static String COMPLAINT_ERROR_7403 = "7403";
//	public static String COMPLAINT_ERROR_7403_DES = "投诉状态不能为空！";
//
//	// 7501--7599
//	public static String COMPLAINT_ERROR_7501 = "7501";
//	public static String COMPLAINT_ERROR_7501_DES = "未找到用户对应的投诉状态(0/1)！";
//	public static String COMPLAINT_ERROR_7502 = "7502";
//	public static String COMPLAINT_ERROR_7502_DES = "未找到用户的投诉信息！";
//	public static String COMPLAINT_ERROR_7503 = "7503";
//	public static String COMPLAINT_ERROR_7503_DES = "投诉状态不能为空！";
//
//	// 7601—7699
//	public static String COMPLAINT_ERROR_7601 = "7601";
//	public static String COMPLAINT_ERROR_7601_DES = "添加用户投诉失败";
//	public static String COMPLAINT_ERROR_7602 = "7602";
//	public static String COMPLAINT_ERROR_7602_DES = "用户投诉内容不能为空";
//	public static String COMPLAINT_ERROR_7603 = "7603";
//	public static String COMPLAINT_ERROR_7603_DES = "投诉内容长度不能超过500";
//
//	// 7701--7799
//	public static String COMPLAINT_ERROR_7701 = "7701";
//	public static String COMPLAINT_ERROR_7701_DES = "投诉单号不能为空";
//	public static String COMPLAINT_ERROR_7702 = "7702";
//	public static String COMPLAINT_ERROR_7702_DES = "投诉状态不能为空";
//	public static String COMPLAINT_ERROR_7703 = "7703";
//	public static String COMPLAINT_ERROR_7703_DES = "备注不能为空";
//	public static String COMPLAINT_ERROR_7704 = "7704";
//	public static String COMPLAINT_ERROR_7704_DES = "处理用户投诉失败！";
//	public static String COMPLAINT_ERROR_7705 = "7705";
//	public static String COMPLAINT_ERROR_7705_DES = "投诉状态处理错误(0/1)！";
//	public static String COMPLAINT_ERROR_7706 = "7706";
//	public static String COMPLAINT_ERROR_7706_DES = "投诉单号不存在！";
//
//	// 801-899
//	public static String ROLE_ERROR_801 = "801";
//	public static String ROLE_ERROR_801_DES = "查询角色名称不存在";
//
//	// 701--799
//	public static String ROLE_ERROR_701 = "701";
//	public static String ROLE_ERROR_701_DES = "角色名称不能为空";
//	
//	public static String ROLE_ERROR_702 = "702";
//	public static String ROLE_ERROR_702_DES = "角色名称不允许重复";
//
//	// 1001-1099
//	public static String ROLE_ERROR_1001 = "1001";
//	public static String ROLE_ERROR_1001_DES = "角色id不能为空";
//	public static String ROLE_ERROR_1002 = "1002";
//	public static String ROLE_ERROR_1002_DES = "角色存在对应的用户 不允许删除";
//
//	// 1401-1499
//	public static String AUTH_ERROR_1401 = "1401";
//	public static String AUTH_ERROR_1401_DES = "查询权限名不存在";
//
//	// 1701--1799
//	public static String AUTH_ERROR_1701 = "1701";
//	public static String AUTH_ERROR_1701_DES = "权限id不能为空";
//	public static String AUTH_ERROR_1702 = "1702";
//	public static String AUTH_ERROR_1702_DES = "权限存在对应的角色 不允许删除";
//	
//	
//	// 901--999
//	public static String ROLE_ERROR_901 = "901";
//	public static String ROLE_ERROR_901_DES = "角色id不能为空";
//	public static String ROLE_ERROR_902 = "902";
//	public static String ROLE_ERROR_902_DES = "角色描述不能为空";
//	public static String ROLE_ERROR_903 = "903";
//	public static String ROLE_ERROR_903_DES = "角色名称不能为空";
//
//
//	public static String AUTH__ERROR_1502= "1502";
//	public static String AUTH__ERROR_1502_DES = "地址不能为空";
//	public static String AUTH__ERROR_1503 = "1503";
//	public static String AUTH__ERROR_1503_DES = "权限名称不能为空";
//	public static String AUTH__ERROR_1504 = "1504";
//	public static String AUTH__ERROR_1504_DES = "权限名称不允许重复";
//	
//	//3801--3899
//	public static String USER_ERROR_3801 = "3801";
//	public static String USER_ERROR_3801_DES = "合作伙伴id不能为空！";
//	public static String USER_ERROR_3802 = "3802";
//	public static String USER_ERROR_3802_DES = "安全码不能为空！";
//	public static String USER_ERROR_3803 = "3803";
//	public static String USER_ERROR_3803_DES = "支付宝账号不能为空！";
//
//	//1101--1199
//	public static String AUTH__ERROR_1101= "1101";
//	public static String AUTH__ERROR_1101_DES = "权限不能为空";
//
//	//产品定价的错误码4801--4899
//	public static String SET_PRODUCT_PRICE_ERR_4801 = "4801";
//	public static String SET_PRODUCT_PRICE_ERR_4801_DES = "起始时间不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4802 = "4802";
//	public static String SET_PRODUCT_PRICE_ERR_4802_DES = "产品不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4803 = "4803";
//	public static String SET_PRODUCT_PRICE_ERR_4803_DES = "买卖类型不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4804 = "4804";
//	public static String SET_PRODUCT_PRICE_ERR_4804_DES = "周期不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4805 = "4805";
//	public static String SET_PRODUCT_PRICE_ERR_4805_DES = "代理价不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4806 = "4806";
//	public static String SET_PRODUCT_PRICE_ERR_4806_DES = "零售价不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4808 = "4808";
//	public static String SET_PRODUCT_PRICE_ERR_4808_DES = "生效时间不能小于当前时间";
//	public static String SET_PRODUCT_PRICE_ERR_4809 = "4809";
//	public static String SET_PRODUCT_PRICE_ERR_4809_DES = "生效时间不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4807 = "4807";
//	public static String SET_PRODUCT_PRICE_ERR_4807_DES = "价格id数据错误";
//	public static String SET_PRODUCT_PRICE_ERR_4810 = "4810";
//	public static String SET_PRODUCT_PRICE_ERR_4810_DES = "零售价不能为空";
//	public static String SET_PRODUCT_PRICE_ERR_4811 = "4811";
//	public static String SET_PRODUCT_PRICE_ERR_4811_DES = "生效时间不能大于失效时间";
//	
//	public static String PRODUCT_ID_ERR_13801="13801";
//	public static String PRODUCT_ID_ERR_13801_DES="参数错误，不能没有产品ID参数!";
//	public static String PRODUCT_ID_ERR_13802="13802";
//	public static String PRODUCT_ID_ERR_13802_DES="没有产品的销售类型信息!";
//	public static String PRODUCT_ID_ERR_13803="13803";
//	public static String PRODUCT_ID_ERR_13803_DES="没有产品的租赁周期信息!";
//	
//	//15201-15299
//	public static String USER_RECHARGE_INFO_ERR_15201="15201";
//	public static String USER_RECHARGE_INFO_ERR_15201_DES="没有可用的充值方式";
//	
//	
//	//16401-16499
//	public static String SET_WEB_ADDRESS_SUCCESS_16400="16400";
//	public static String SET_WEB_ADDRESS_SUCCESS_16400_DES="设置经销商卖场地址成功！";
//	public static String SET_WEB_ADDRESS_ERR_16401="16401";
//	public static String SET_WEB_ADDRESS_ERR_16401_DES="用户ID参数不能为空！";
//	public static String SET_WEB_ADDRESS_ERR_16402="16402";
//	public static String SET_WEB_ADDRESS_ERR_16402_DES="网站地址不能为空";
//	public static String SET_WEB_ADDRESS_ERR_16403="16403";
//	public static String SET_WEB_ADDRESS_ERR_16403_DES="下载地址不能为空";
	
	
	
	
	
	
}
