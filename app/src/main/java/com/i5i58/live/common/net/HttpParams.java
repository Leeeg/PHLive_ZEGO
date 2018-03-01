package com.i5i58.live.common.net;

import java.util.Map;

public class HttpParams {
	public static final String POST = "POST";
	public static final String GET = "GET";

	private String url;
	private Map<String, Object> map;
	private String method;
	private String tag;
	private String accid;
	private String token;

	public String getAccid() {
		return accid;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}
