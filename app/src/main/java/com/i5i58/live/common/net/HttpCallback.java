package com.i5i58.live.common.net;

import org.json.JSONObject;

public interface HttpCallback {
	void success(JSONObject jsonObject, boolean success);
}
