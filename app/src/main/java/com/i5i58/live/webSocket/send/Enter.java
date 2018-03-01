package com.i5i58.live.webSocket.send;

/**
 * Created by Lee on 2017/4/26.
 */

public class Enter {

    private String cmd;
    private Params params;

    public String getCmd() {
        return cmd;
    }

    public Enter setCmd(String cmd) {
        this.cmd = cmd;
        return this;
    }

    public Params getParams() {
        return params;
    }

    public Enter setParams(Params params) {
        this.params = params;
        return this;
    }


}
