package com.i5i58.live.model.entity.rnConfig;

import com.google.gson.Gson;

/**
 * Created by Lee on 2017/4/11.
 */

public class React {


    /**
     * id : SerIndex
     * Node : main
     * name : 0
     * icon :
     * module : SerIndex
     * rnZip :
     * version : operating
     * type : fixed
     */

    private String id;
    private String node;
    private String name;
    private String icon;
    private String module;
    private String rnZip;
    private String version;
    private String type;
    private String section;

    public static React objectFromData(String str) {

        return new Gson().fromJson(str, React.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getRnZip() {
        return rnZip;
    }

    public void setRnZip(String rnZip) {
        this.rnZip = rnZip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
