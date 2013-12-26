package com.btcoin;

import java.io.IOException;

public class BtcoinException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	private String errCode;
	
	public BtcoinException(String errCode,String message, Throwable throwable) {
		super(message, throwable);
		this.errCode = errCode;
	}

	public BtcoinException(String errCode,String message) {
		super(message);
		this.errCode = errCode;
	}

	public BtcoinException(String errCode,Throwable throwable) {
		super(throwable);
		this.errCode = errCode;
	}
	
	public String getErrCode(){
		return errCode;
	}
	@Override
	public String getMessage() {
		return String.format("[C%s]%s",errCode,super.getMessage());
	}
}
