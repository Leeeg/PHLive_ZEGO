package com.i5i58.live.model.entity.rnConfig;

import java.util.ArrayList;

/**
 * Created by Lee on 2017/4/7.
 */

public class RNConfig {

    private String rnConfigVersion = "0";
    private String rnZip;
    private ArrayList<React> react;

    public String getRnConfigVersion() {
        return rnConfigVersion;
    }

    public void setRnConfigVersion(String rnConfigVersion) {
        this.rnConfigVersion = rnConfigVersion;
    }

    public String getRnZip() {
        return rnZip;
    }

    public void setRnZip(String rnZip) {
        this.rnZip = rnZip;
    }

    public ArrayList<React> getReact() {
        return react;
    }

    public void setReact(ArrayList<React> react) {
        this.react = react;
    }
}
