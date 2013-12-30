package com.btcoin.common;

public class Resp {
	String recode;
	String message;
	Object result;

	public Resp(String recode, String message) {
		this.recode = recode;
		this.message = message;
	}
	
	public Resp(String recode, String message, Object result) {
		this.recode = recode;
		this.message = message;
		this.result = result;
	}

	public String getRecode() {
		return recode;
	}

	public void setRecode(String recode) {
		this.recode = recode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}