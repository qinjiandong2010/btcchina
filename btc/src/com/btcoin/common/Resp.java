package com.btcoin.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.btcoin.utils.StringUtil;

public class Resp {
	private Logger log = Logger.getLogger( getClass().getName() );
	String result;
	String summary;
	List list;
	Map condition;
	Map datamap;
	
	public Resp() {

	}

	public static String setMsg(Object code, Object _return, String messsage) {
		JSONObject resp = new JSONObject();
		resp.put( "result", Integer.parseInt( code.toString()) );
		resp.put( "return", StringUtil.isNullOrEmpty( _return ) ? new JSONObject() : _return );
		resp.put( "message", messsage );
		return resp.toString();
	}

	public Resp(String result, String summary) {
		this.result = result;
		this.summary = summary;
	}

	public Resp(String result, String summary, List list, Map condition, Map datamap) {
		this.result = result;
		this.summary = summary;
		this.list = list;
		this.condition = condition;
		this.datamap = datamap;
	}

	public Map getDatamap() {
		return datamap;
	}

	public void setDatamap(Map datamap) {
		this.datamap = datamap;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Map getCondition() {
		return condition;
	}

	public void setCondition(Map condition) {
		this.condition = condition;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String toJSONString() {
		JSONObject resp = new JSONObject();
		resp.put( "result", this.result );
		resp.put( "summary", this.summary );
		JSONObject body = new JSONObject();
		if (ErrorCode.SUCCESS.equals( this.result )) {
			if (this.condition != null && condition.size() > 0) {
				JSONObject cond = new JSONObject();
				Iterator it = condition.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					Object value = condition.get( key );
					cond.put( key, value );
				}
				body.put( "params", cond );
			}

			if (list != null && list.size() > 0) {
				JSONArray data = new JSONArray();
				for (int i = 0; i < list.size(); i++)
					data.add( list.get( i ) );
				body.put( "data", data );
			}

			if (this.datamap != null && datamap.size() > 0) { 
				Iterator it = datamap.keySet().iterator();
				while (it.hasNext()) {
					Object key = it.next();
					Object value = datamap.get( key );
					body.put( key, value );
				}
			}

		}
		resp.put( "return", body );
		log.debug( "resp:" + resp.toString() );
		return resp.toString();
	}
}