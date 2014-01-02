package com.btcoin.common;

import net.sf.json.JSONObject;

public class Resp {
	Integer recode;
	String message;
	Object result;

	public Resp(int recode, String message) {
		this.recode = recode;
		this.message = message;
	}
	
	public Resp(int recode, String message, Object result) {
		this.recode = recode;
		this.message = message;
		this.result = result;
	}

	public int getRecode() {
		return recode;
	}

	public void setRecode(int recode) {
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

	public String toString(){
		return JSONObject.fromObject(this).toString();
	}
}